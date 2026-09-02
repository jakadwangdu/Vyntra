package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.AppDatabase
import com.example.data.local.MealEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterLogEntity
import com.example.data.local.WorkoutLogEntity
import kotlinx.coroutines.flow.first
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
class ExampleRobolectricTest {

    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun read_string_from_context() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Vyntra", appName)
    }

    @Test
    fun insert_and_read_meal() = runBlocking {
        val meal = MealEntity(
            name = "Panna Cotta",
            mealType = "Snacks",
            calories = 260,
            protein = 12,
            carbs = 24,
            fat = 20,
            portionGrams = 140,
            date = "2026-09-02"
        )
        val id = db.mealDao().insertMeal(meal)
        assertTrue(id > 0)

        val meals = db.mealDao().getMealsByDate("2026-09-02").first()
        assertEquals(1, meals.size)
        assertEquals("Panna Cotta", meals[0].name)
        assertEquals(260, meals[0].calories)
    }

    @Test
    fun test_user_profile_and_water_log() = runBlocking {
        val profile = UserProfileEntity(
            id = 1,
            name = "Yohanan Rashad",
            dailyCalorieTarget = 2400,
            goal = "CUT"
        )
        db.userProfileDao().insertOrUpdate(profile)
        val saved = db.userProfileDao().getUserProfile().first()
        assertNotNull(saved)
        assertEquals("Yohanan Rashad", saved?.name)
        assertEquals("CUT", saved?.goal)

        val water = WaterLogEntity(date = "2026-09-02", flOz = 32, glasses = 4)
        db.waterLogDao().insertOrUpdateWaterLog(water)
        val savedWater = db.waterLogDao().getWaterLog("2026-09-02").first()
        assertEquals(32, savedWater?.flOz)
        assertEquals(4, savedWater?.glasses)
    }
}
