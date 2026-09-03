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
        if (profile == null) {
            profileDao.insertOrUpdate(
                UserProfileEntity(
                    id = 1,
                    name = "Athlete",
                    age = 25,
                    gender = "Male",
                    weightKg = 75f,
                    heightCm = 178f,
                    activityLevel = "Moderate",
                    goal = "MAINTAIN",
                    dietaryRestriction = "Both Veg & Non-Veg",
                    dailyCalorieTarget = 2400,
                    dailyProteinTarget = 160,
                    dailyCarbsTarget = 220,
                    dailyFatTarget = 65,
                    dailyWaterTargetFlOz = 64,
                    customDietPlanJson = "",
                    onboardingCompleted = false
                )
            )
        }
    }
}
