package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.local.MealEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterLogEntity
import com.example.data.local.WorkoutLogEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NutriLensRepository(private val database: AppDatabase) {
    private val mealDao = database.mealDao()
    private val profileDao = database.userProfileDao()
    private val waterDao = database.waterLogDao()
    private val workoutDao = database.workoutLogDao()

    fun getMealsByDate(date: String): Flow<List<MealEntity>> = mealDao.getMealsByDate(date)

    val allMeals: Flow<List<MealEntity>> = mealDao.getAllMeals()

    val userProfile: Flow<UserProfileEntity?> = profileDao.getUserProfile()

    fun getWaterLog(date: String): Flow<WaterLogEntity?> = waterDao.getWaterLog(date)

    fun getWorkoutsByDate(date: String): Flow<List<WorkoutLogEntity>> = workoutDao.getWorkoutsByDate(date)

    suspend fun insertMeal(meal: MealEntity): Long = mealDao.insertMeal(meal)

    suspend fun deleteMealById(id: Int) = mealDao.deleteMealById(id)

    suspend fun saveUserProfile(profile: UserProfileEntity) = profileDao.insertOrUpdate(profile)

    suspend fun updateWaterLog(date: String, deltaGlasses: Int, glassSizeFlOz: Int = 8) {
        val existing = waterDao.getWaterLog(date).firstOrNull()
        val currentGlasses = existing?.glasses ?: 0
        val currentFlOz = existing?.flOz ?: 0
        val newGlasses = (currentGlasses + deltaGlasses).coerceAtLeast(0)
        val newFlOz = (currentFlOz + deltaGlasses * glassSizeFlOz).coerceAtLeast(0)
        waterDao.insertOrUpdateWaterLog(
            WaterLogEntity(
                date = date,
                flOz = newFlOz,
                glasses = newGlasses
            )
        )
    }

    suspend fun logWorkout(workout: WorkoutLogEntity): Long = workoutDao.insertWorkout(workout)

    suspend fun seedInitialDataIfEmpty() {
        val profile = profileDao.getUserProfile().firstOrNull()
        val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (profile == null) {
            profileDao.insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    name = "Yohanan Rashad",
                    age = 26,
                    weightKg = 76f,
                    heightCm = 180f,
                    activityLevel = "Moderate",
                    goal = "CUT",
                    dietaryRestriction = "None",
                    dailyCalorieTarget = 2400,
                    dailyProteinTarget = 150,
                    dailyCarbsTarget = 220,
                    dailyFatTarget = 65,
                    dailyWaterTargetFlOz = 64,
                    onboardingCompleted = true
                )
            )
        }

        val todayMeals = mealDao.getMealsByDate(todayStr).firstOrNull()
        if (todayMeals.isNullOrEmpty()) {
            // Seed initial breakfast and snack to match reference image UI
            mealDao.insertMeal(
                MealEntity(
                    name = "Avocado & Egg Toast",
                    mealType = "Breakfast",
                    calories = 365,
                    protein = 18,
                    carbs = 34,
                    fat = 17,
                    portionGrams = 180,
                    date = todayStr,
                    imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=500",
                    description = "Whole grain sourdough topped with smashed avocado, poached eggs, and microgreens.",
                    ingredients = "Sourdough Bread, Hass Avocado, Poached Eggs, Olive Oil, Chili Flakes",
                    micronutrients = "Potassium: 420mg, Iron: 12% DV, Vitamin E: 15% DV"
                )
            )
            mealDao.insertMeal(
                MealEntity(
                    name = "Greek Yogurt Bowl",
                    mealType = "Breakfast",
                    calories = 195,
                    protein = 16,
                    carbs = 22,
                    fat = 5,
                    portionGrams = 200,
                    date = todayStr,
                    imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=500",
                    description = "Strained Greek yogurt loaded with blueberries, chia seeds, and raw honeycomb.",
                    ingredients = "Greek Yogurt, Fresh Blueberries, Chia Seeds, Honey",
                    micronutrients = "Calcium: 20% DV, Probiotics: 1B CFU, Vitamin C: 10% DV"
                )
            )
            // Seed 1 workout
            workoutDao.insertWorkout(
                WorkoutLogEntity(
                    date = todayStr,
                    title = "Morning HIIT & Core",
                    category = "Fat Burn HIIT",
                    durationMinutes = 28,
                    caloriesBurned = 345
                )
            )
            // Seed water log (4 glasses = 32 fl oz)
            waterDao.insertOrUpdateWaterLog(
                WaterLogEntity(
                    date = todayStr,
                    flOz = 32,
                    glasses = 4
                )
            )
        }
    }
}
