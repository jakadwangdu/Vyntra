package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.MealEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterLogEntity
import com.example.data.local.WorkoutLogEntity
import com.example.data.model.DietPlanGenerator
import com.example.data.model.FoodScanResult
import com.example.data.remote.GeminiService
import com.example.data.repository.NutriLensRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RealWorldAccuracyAndSecurityTest {

    private lateinit var db: AppDatabase
    private lateinit var repository: NutriLensRepository
    private val geminiService = GeminiService()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = NutriLensRepository(db)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testDietPlanGenerator_Accuracy_MifflinStJeor() {
        // Test standard male 25yo, 75kg, 178cm, Moderate activity, Maintain
        // BMR = 10 * 75 + 6.25 * 178 - 5 * 25 + 5 = 750 + 1112.5 - 125 + 5 = 1742.5 -> 1742
        // TDEE = 1742 * 1.45 = 2525 kcal
        val metrics = DietPlanGenerator.calculateMetrics(
            age = 25,
            gender = "Male",
            weightKg = 75f,
            heightCm = 178f,
            activityLevel = "Moderate",
            goal = "MAINTAIN"
        )

        assertTrue("BMR should be realistic (approx 1700-1800)", metrics.bmr in 1700..1800)
        assertTrue("TDEE should be realistic (approx 2400-2600)", metrics.tdee in 2400..2600)
        assertTrue("Target calories should equal TDEE for maintain", metrics.targetCalories == metrics.tdee)
        assertTrue("Protein should be between 130g and 160g", metrics.proteinGrams in 130..160)
        assertTrue("Fats should be 20-30% of total calories", metrics.fatGrams in 50..90)
        assertTrue("Carbs should be balanced", metrics.carbsGrams in 200..400)
        assertTrue("Water target should be at least 64 fl oz", metrics.waterFlOz >= 64)
    }

    @Test
    fun testDietPlanGenerator_ExtremeInputs_DoesNotCrashOrOverflow() {
        // Extreme zero/negative inputs
        val extremeLow = DietPlanGenerator.calculateMetrics(
            age = 0,
            gender = "Other",
            weightKg = 10f,
            heightCm = 50f,
            activityLevel = "Unknown",
            goal = "LOSE_WEIGHT"
        )
        assertTrue(extremeLow.targetCalories >= 1000)
        assertTrue(extremeLow.proteinGrams >= 50)
        assertTrue(extremeLow.waterFlOz >= 64)

        // Extreme high inputs
        val extremeHigh = DietPlanGenerator.calculateMetrics(
            age = 150,
            gender = "Male",
            weightKg = 300f,
            heightCm = 250f,
            activityLevel = "Very Active",
            goal = "BULK"
        )
        assertTrue(extremeHigh.targetCalories in 1500..8000)
        assertTrue(extremeHigh.proteinGrams in 60..300)
    }

    @Test
    fun testWaterTracking_NoNegativeUnderflow() = runBlocking {
        val testDate = "2026-09-04"
        // Start at 0
        repository.updateWaterLog(testDate, deltaGlasses = -2, glassSizeFlOz = 8)
        val waterLog = repository.getWaterLog(testDate).firstOrNull()
        assertNotNull(waterLog)
        assertEquals("Glasses should not go below 0", 0, waterLog?.glasses)
        assertEquals("Fl oz should not go below 0", 0, waterLog?.flOz)

        // Add 2 glasses
        repository.updateWaterLog(testDate, deltaGlasses = 2, glassSizeFlOz = 8)
        val updated = repository.getWaterLog(testDate).firstOrNull()
        assertEquals(2, updated?.glasses)
        assertEquals(16, updated?.flOz)
    }

    @Test
    fun testFoodLogging_MacroAggregationAccuracy() = runBlocking {
        val testDate = "2026-09-04"
        val meal1 = MealEntity(
            name = "Chicken Rice",
            mealType = "Lunch",
            calories = 500,
            protein = 40,
            carbs = 60,
            fat = 10,
            portionGrams = 300,
            date = testDate
        )
        val meal2 = MealEntity(
            name = "Greek Yogurt",
            mealType = "Snacks",
            calories = 150,
            protein = 18,
            carbs = 8,
            fat = 3,
            portionGrams = 170,
            date = testDate
        )

        repository.insertMeal(meal1)
        repository.insertMeal(meal2)

        val meals = repository.getMealsByDate(testDate).first()
        assertEquals(2, meals.size)
        assertEquals(650, meals.sumOf { it.calories })
        assertEquals(58, meals.sumOf { it.protein })
        assertEquals(68, meals.sumOf { it.carbs })
        assertEquals(13, meals.sumOf { it.fat })
    }

    @Test
    fun testGeminiFoodSearch_OfflineFallbackAccuracy() = runBlocking {
        // Test searching Biryani via fallback heuristics
        val result = geminiService.analyzeFoodByName("Paneer Butter Masala").getOrNull()
        assertNotNull(result)
        assertTrue("Calories should be estimated realistically", result!!.calories in 300..700)
        assertTrue("Protein should be calculated", result.protein > 10)
        assertTrue("Carbs should be calculated", result.carbs > 15)
        assertTrue("Health benefits should be provided", result.healthBenefits.isNotEmpty())
        assertTrue("Ingredients should be present", result.ingredients.isNotEmpty())
    }

    @Test
    fun testGeminiFoodSearch_EmptyQueryHandling() = runBlocking {
        val result = geminiService.analyzeFoodByName("")
        assertTrue("Blank query should return failure safely without crashing", result.isFailure)
    }
}
