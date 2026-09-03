package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class MealEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val portionGrams: Int = 100,
    val date: String, // yyyy-MM-dd
    val timestamp: Long = System.currentTimeMillis(),
    val imageUrl: String = "",
    val description: String = "",
    val ingredients: String = "",
    val micronutrients: String = ""
)

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey
    val id: Int = 1,
    val name: String = "Athlete",
    val age: Int = 25,
    val gender: String = "Male", // Male, Female, Other
    val weightKg: Float = 75f,
    val heightCm: Float = 178f,
    val activityLevel: String = "Moderate", // Sedentary, Moderate, Active, Very Active
    val goal: String = "MAINTAIN", // BULK, MAINTAIN, LOSE_WEIGHT
    val dietaryRestriction: String = "Both Veg & Non-Veg", // Vegetarian, Vegan, Non-Vegetarian, Both Veg & Non-Veg
    val dailyCalorieTarget: Int = 2400,
    val dailyProteinTarget: Int = 160,
    val dailyCarbsTarget: Int = 220,
    val dailyFatTarget: Int = 65,
    val dailyWaterTargetFlOz: Int = 64,
    val customDietPlanJson: String = "",
    val onboardingCompleted: Boolean = false
)

@Entity(tableName = "water_logs")
data class WaterLogEntity(
    @PrimaryKey
    val date: String, // yyyy-MM-dd
    val flOz: Int = 0,
    val glasses: Int = 0
)

@Entity(tableName = "workout_logs")
data class WorkoutLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String, // yyyy-MM-dd
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val caloriesBurned: Int,
    val timestamp: Long = System.currentTimeMillis()
)
