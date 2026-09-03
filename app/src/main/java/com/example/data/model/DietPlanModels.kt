package com.example.data.model

data class DietPlanMeal(
    val mealType: String, // Breakfast, Lunch, Dinner, Snack
    val title: String,
    val portion: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val ingredients: List<String>,
    val preparation: String,
    val dietaryTag: String,
    val imageUrl: String = ""
)

data class CustomDietPlan(
    val goal: String, // BULK, MAINTAIN, LOSE_WEIGHT
    val dietaryPreference: String, // Vegetarian, Vegan, Non-Vegetarian, Both Veg & Non-Veg
    val targetCalories: Int,
    val targetProtein: Int,
    val targetCarbs: Int,
    val targetFat: Int,
    val hydrationTargetFlOz: Int,
    val analysisSummary: String,
    val strategyTips: List<String>,
    val meals: List<DietPlanMeal>
)
