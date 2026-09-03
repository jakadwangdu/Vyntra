package com.example.data.model

object DietPlanGenerator {

    data class CalculatedMetrics(
        val bmr: Int,
        val tdee: Int,
        val targetCalories: Int,
        val proteinGrams: Int,
        val carbsGrams: Int,
        val fatGrams: Int,
        val waterFlOz: Int
    )

    fun calculateMetrics(
        age: Int,
        gender: String,
        weightKg: Float,
        heightCm: Float,
        activityLevel: String,
        goal: String,
        customCalorieTarget: Int? = null
    ): CalculatedMetrics {
        val safeWeight = weightKg.coerceAtLeast(30f)
        val safeHeight = heightCm.coerceAtLeast(100f)
        val safeAge = age.coerceIn(12, 100)

        // Mifflin-St Jeor equation
        val bmr = when (gender) {
            "Female" -> (10 * safeWeight + 6.25f * safeHeight - 5 * safeAge - 161).toInt()
            "Male" -> (10 * safeWeight + 6.25f * safeHeight - 5 * safeAge + 5).toInt()
            else -> (10 * safeWeight + 6.25f * safeHeight - 5 * safeAge - 78).toInt()
        }

        val activityMultiplier = when (activityLevel) {
            "Sedentary" -> 1.20f
            "Moderate" -> 1.45f
            "Active" -> 1.65f
            "Very Active" -> 1.85f
            else -> 1.45f
        }

        val tdee = (bmr * activityMultiplier).toInt()

        val calculatedCalories = when (goal) {
            "BULK" -> tdee + 400
            "LOSE_WEIGHT" -> (tdee - 500).coerceAtLeast(1350)
            else -> tdee // MAINTAIN
        }

        val targetCalories = if (customCalorieTarget != null && customCalorieTarget >= 1000) {
            customCalorieTarget
        } else {
            calculatedCalories
        }

        // Protein targets based on fitness science:
        // BULK: 2.0g/kg, LOSE_WEIGHT: 2.2g/kg (muscle preservation), MAINTAIN: 1.8g/kg
        val proteinPerKg = when (goal) {
            "LOSE_WEIGHT" -> 2.2f
            "BULK" -> 2.0f
            else -> 1.85f
        }
        val proteinGrams = (safeWeight * proteinPerKg).toInt().coerceIn(60, 260)
        val proteinCalories = proteinGrams * 4

        // Healthy Fats: 25% of total intake
        val fatCalories = (targetCalories * 0.25f).toInt()
        val fatGrams = (fatCalories / 9).coerceIn(35, 120)

        // Carbohydrates: remaining calories
        val carbsCalories = (targetCalories - proteinCalories - fatCalories).coerceAtLeast(300)
        val carbsGrams = (carbsCalories / 4).coerceIn(70, 500)

        // Water: standard 0.5 fl oz per lb of body weight
        val waterFlOz = ((safeWeight * 2.20462f) * 0.55f).toInt().coerceIn(64, 130)

        return CalculatedMetrics(
            bmr = bmr,
            tdee = tdee,
            targetCalories = targetCalories,
            proteinGrams = proteinGrams,
            carbsGrams = carbsGrams,
            fatGrams = fatGrams,
            waterFlOz = waterFlOz
        )
    }

    fun generatePlan(
        name: String,
        age: Int,
        gender: String,
        weightKg: Float,
        heightCm: Float,
        activityLevel: String,
        goal: String,
        dietaryPreference: String,
        customCalorieTarget: Int? = null
    ): CustomDietPlan {
        val metrics = calculateMetrics(
            age = age,
            gender = gender,
            weightKg = weightKg,
            heightCm = heightCm,
            activityLevel = activityLevel,
            goal = goal,
            customCalorieTarget = customCalorieTarget
        )

        val totalCals = metrics.targetCalories
        val pG = metrics.proteinGrams
        val cG = metrics.carbsGrams
        val fG = metrics.fatGrams

        val goalLabel = when (goal) {
            "BULK" -> "Hypertrophy & Mass Bulk"
            "LOSE_WEIGHT" -> "Fat Loss & Physique Leaning"
            else -> "Athletic Physique Maintenance"
        }

        val dietLabel = when (dietaryPreference) {
            "Vegetarian" -> "Vegetarian"
            "Vegan" -> "100% Plant-Based Vegan"
            "Non-Vegetarian" -> "High-Protein Non-Vegetarian"
            else -> "Flexitarian (Veg & Non-Veg)"
        }

        val analysisSummary = "Based on your body metrics ($weightKg kg, $heightCm cm, $age yrs $gender, $activityLevel activity), " +
                "your estimated maintenance (TDEE) is ${metrics.tdee} kcal. " +
                if (goal == "BULK") "We programmed a calculated surplus target of $totalCals kcal (+400 kcal) with $pG g protein to maximize lean muscle synthesis."
                else if (goal == "LOSE_WEIGHT") "We programmed a calibrated deficit of $totalCals kcal (-500 kcal) with elevated protein ($pG g) to strip body fat while sparing muscle tissue."
                else "We programmed an exact energy-balance target of $totalCals kcal with $pG g protein to maintain low body fat, sustain performance, and preserve lean physique."

        val strategyTips = when (goal) {
            "BULK" -> listOf(
                "Eat consistently every 3.5-4 hours to fuel steady protein synthesis.",
                "Prioritize complex carbohydrates around training sessions for explosive power.",
                "Drink at least ${metrics.waterFlOz} fl oz of water to maintain cellular hydration and nutrient delivery."
            )
            "LOSE_WEIGHT" -> listOf(
                "Keep protein high ($pG g) at every meal to preserve muscle fullness in a deficit.",
                "Prioritize fibrous greens and whole foods for maximum fullness and satiety.",
                "Consume 16 fl oz of water 20 minutes prior to main meals to regulate appetite."
            )
            else -> listOf(
                "Maintain consistent macronutrient timing to sustain high daily energy output.",
                "Prioritize micronutrient diversity: dark leafy greens, colorful berries, and healthy fats.",
                "Track portion sizes faithfully to remain squarely at caloric equilibrium."
            )
        }

        val meals = createMealsForConfiguration(
            goal = goal,
            diet = dietaryPreference,
            totalCalories = totalCals,
            totalProtein = pG,
            totalCarbs = cG,
            totalFat = fG
        )

        return CustomDietPlan(
            goal = goal,
            dietaryPreference = dietaryPreference,
            targetCalories = totalCals,
            targetProtein = pG,
            targetCarbs = cG,
            targetFat = fG,
            hydrationTargetFlOz = metrics.waterFlOz,
            analysisSummary = analysisSummary,
            strategyTips = strategyTips,
            meals = meals
        )
    }

    private fun createMealsForConfiguration(
        goal: String,
        diet: String,
        totalCalories: Int,
        totalProtein: Int,
        totalCarbs: Int,
        totalFat: Int
    ): List<DietPlanMeal> {
        // Approximate caloric division:
        // Breakfast: 26%, Lunch: 34%, Snack: 15%, Dinner: 25%
        val bCals = (totalCalories * 0.26f).toInt()
        val lCals = (totalCalories * 0.34f).toInt()
        val sCals = (totalCalories * 0.15f).toInt()
        val dCals = (totalCalories * 0.25f).toInt()

        val bProt = (totalProtein * 0.25f).toInt()
        val lProt = (totalProtein * 0.35f).toInt()
        val sProt = (totalProtein * 0.15f).toInt()
        val dProt = (totalProtein * 0.25f).toInt()

        val bCarb = (totalCarbs * 0.28f).toInt()
        val lCarb = (totalCarbs * 0.35f).toInt()
        val sCarb = (totalCarbs * 0.15f).toInt()
        val dCarb = (totalCarbs * 0.22f).toInt()

        val bFat = (totalFat * 0.25f).toInt()
        val lFat = (totalFat * 0.30f).toInt()
        val sFat = (totalFat * 0.20f).toInt()
        val dFat = (totalFat * 0.25f).toInt()

        return when {
            diet.contains("Vegan", ignoreCase = true) -> listOf(
                DietPlanMeal(
                    mealType = "Breakfast",
                    title = "Turmeric Tofu Scramble & Avocado Toast",
                    portion = "320g",
                    calories = bCals,
                    protein = bProt,
                    carbs = bCarb,
                    fat = bFat,
                    ingredients = listOf("Firm Tofu (200g)", "Nutritional Yeast", "Baby Spinach", "1/2 Hass Avocado", "Sprouted Sourdough"),
                    preparation = "Sauté crumbled tofu in turmeric, black salt, and spinach. Serve hot over sliced avocado on toasted sprouted bread.",
                    dietaryTag = "Vegan",
                    imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=500"
                ),
                DietPlanMeal(
                    mealType = "Lunch",
                    title = "Mediterranean Quinoa & Chickpea Power Bowl",
                    portion = "380g",
                    calories = lCals,
                    protein = lProt,
                    carbs = lCarb,
                    fat = lFat,
                    ingredients = listOf("Cooked Tri-Color Quinoa", "Roasted Chickpeas", "Steamed Broccoli", "Kalamata Olives", "Tahini Lemon Vinaigrette"),
                    preparation = "Toss warm quinoa with roasted chickpeas and steamed greens. Drizzle with garlic tahini dressing.",
                    dietaryTag = "Vegan",
                    imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500"
                ),
                DietPlanMeal(
                    mealType = "Snack",
                    title = "Plant Protein Smoothie with Chia & Berries",
                    portion = "300ml",
                    calories = sCals,
                    protein = sProt,
                    carbs = sCarb,
                    fat = sFat,
                    ingredients = listOf("Pea Protein Isolate", "Almond Milk", "Chia Seeds", "Wild Blueberries", "Flaxseed Powder"),
                    preparation = "Blend on high speed until frosty and smooth. Consume immediately as pre or post-training fuel.",
                    dietaryTag = "Vegan",
                    imageUrl = "https://images.unsplash.com/photo-1553530666-ba11a7da3888?w=500"
                ),
                DietPlanMeal(
                    mealType = "Dinner",
                    title = "Pan-Seared Tempeh & Ginger Greens Stir-Fry",
                    portion = "340g",
                    calories = dCals,
                    protein = dProt,
                    carbs = dCarb,
                    fat = dFat,
                    ingredients = listOf("Organic Tempeh", "Snap Peas", "Edamame Beans", "Brown Basmati Rice", "Tamari Ginger Glaze"),
                    preparation = "Cube tempeh and sear in sesame oil until golden brown. Toss with crisp snap peas, edamame, and serve over rice.",
                    dietaryTag = "Vegan",
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500"
                )
            )

            diet.equals("Vegetarian", ignoreCase = true) -> listOf(
                DietPlanMeal(
                    mealType = "Breakfast",
                    title = "Greek Yogurt Parfait with Eggs & Sourdough",
                    portion = "310g",
                    calories = bCals,
                    protein = bProt,
                    carbs = bCarb,
                    fat = bFat,
                    ingredients = listOf("0% Plain Greek Yogurt (200g)", "2 Poached Eggs", "Sprouted Sourdough Slice", "Blueberries", "Raw Honey"),
                    preparation = "Layer Greek yogurt with fresh berries and walnuts. Enjoy alongside warm poached pasture eggs on sourdough.",
                    dietaryTag = "Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=500"
                ),
                DietPlanMeal(
                    mealType = "Lunch",
                    title = "Grilled Paneer Tikka with Yellow Dal & Brown Rice",
                    portion = "400g",
                    calories = lCals,
                    protein = lProt,
                    carbs = lCarb,
                    fat = lFat,
                    ingredients = listOf("Low-Fat Paneer (150g)", "Yellow Lentil Dal", "Brown Basmati Rice", "Bell Peppers", "Mint Coriander Raita"),
                    preparation = "Marinate paneer with spiced yogurt and grill until lightly charred. Serve with simmered yellow dal and brown rice.",
                    dietaryTag = "Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1567337710282-00832b415979?w=500"
                ),
                DietPlanMeal(
                    mealType = "Snack",
                    title = "Whey Protein Shake & Spiced Roasted Edamame",
                    portion = "250ml",
                    calories = sCals,
                    protein = sProt,
                    carbs = sCarb,
                    fat = sFat,
                    ingredients = listOf("Whey Isolate (1 scoop)", "Oat Milk", "Roasted Sea-Salt Edamame (40g)", "1/2 Banana"),
                    preparation = "Shake whey with cold oat milk and ice. Pair with crunchy roasted edamame for sustained amino acids.",
                    dietaryTag = "Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1590301157890-4810ed352733?w=500"
                ),
                DietPlanMeal(
                    mealType = "Dinner",
                    title = "Cottage Cheese & Spinach Stuffed Bell Peppers",
                    portion = "350g",
                    calories = dCals,
                    protein = dProt,
                    carbs = dCarb,
                    fat = dFat,
                    ingredients = listOf("Cottage Cheese / Ricotta (180g)", "Baby Spinach", "Bell Peppers", "Quinoa Base", "Cracked Black Pepper"),
                    preparation = "Stuff roasted bell peppers with seasoned cottage cheese, wilted spinach, and quinoa. Bake at 375°F for 18 minutes.",
                    dietaryTag = "Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=500"
                )
            )

            diet.equals("Non-Vegetarian", ignoreCase = true) -> listOf(
                DietPlanMeal(
                    mealType = "Breakfast",
                    title = "Egg White & Turkey Bacon Omelette with Avocado",
                    portion = "320g",
                    calories = bCals,
                    protein = bProt,
                    carbs = bCarb,
                    fat = bFat,
                    ingredients = listOf("3 Whole Eggs + 2 Egg Whites", "Lean Turkey Bacon (2 strips)", "Hass Avocado (1/4)", "Rye Toast", "Cherry Tomatoes"),
                    preparation = "Whisk eggs with herbs, cook gently in virgin olive oil, fold with diced avocado and serve with crisp toast.",
                    dietaryTag = "Non-Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=500"
                ),
                DietPlanMeal(
                    mealType = "Lunch",
                    title = "Herb-Grilled Chicken Breast with Sweet Potato & Broccoli",
                    portion = "420g",
                    calories = lCals,
                    protein = lProt,
                    carbs = lCarb,
                    fat = lFat,
                    ingredients = listOf("Skinless Chicken Breast (200g)", "Baked Sweet Potato", "Steamed Broccoli Florets", "Extra Virgin Olive Oil", "Garlic Rosemary Rub"),
                    preparation = "Grill chicken breast over medium heat until 165°F internal temperature. Serve with roasted sweet potato wedges and broccoli.",
                    dietaryTag = "Non-Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1532550907401-a500c9a57435?w=500"
                ),
                DietPlanMeal(
                    mealType = "Snack",
                    title = "Greek Yogurt Bowl with Almond Butter & Whey",
                    portion = "240g",
                    calories = sCals,
                    protein = sProt,
                    carbs = sCarb,
                    fat = sFat,
                    ingredients = listOf("Greek Yogurt 2%", "Vanilla Whey Isolate", "Almond Butter (1 tbsp)", "Strawberries"),
                    preparation = "Stir whey isolate directly into cold Greek yogurt until thick pudding texture. Top with strawberries and almond butter.",
                    dietaryTag = "Non-Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=500"
                ),
                DietPlanMeal(
                    mealType = "Dinner",
                    title = "Pan-Seared Wild Salmon Fillet with Asparagus & Jasmine Rice",
                    portion = "360g",
                    calories = dCals,
                    protein = dProt,
                    carbs = dCarb,
                    fat = dFat,
                    ingredients = listOf("Wild Alaskan Salmon (180g)", "Steamed Jasmine Rice", "Grilled Asparagus Spears", "Lemon Herb Butter", "Sea Salt"),
                    preparation = "Sear salmon skin-side down in a hot cast-iron skillet for 4 mins, flip for 3 mins. Squeeze fresh lemon over steamed rice and asparagus.",
                    dietaryTag = "Non-Vegetarian",
                    imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=500"
                )
            )

            // Both Veg & Non-Veg (Flexitarian)
            else -> listOf(
                DietPlanMeal(
                    mealType = "Breakfast",
                    title = "Poached Eggs Over Smashed Avocado & Microgreens",
                    portion = "310g",
                    calories = bCals,
                    protein = bProt,
                    carbs = bCarb,
                    fat = bFat,
                    ingredients = listOf("2 Pasture-Raised Eggs", "Whole Grain Sourdough", "Hass Avocado", "Hemp Seeds", "Microgreens"),
                    preparation = "Toast sourdough, layer seasoned mashed avocado, top with two warm runny poached eggs and hemp hearts.",
                    dietaryTag = "Both Veg & Non-Veg",
                    imageUrl = "https://images.unsplash.com/photo-1525351484163-7529414344d8?w=500"
                ),
                DietPlanMeal(
                    mealType = "Lunch",
                    title = "Mediterranean Grilled Chicken & Chickpea Quinoa Salad",
                    portion = "400g",
                    calories = lCals,
                    protein = lProt,
                    carbs = lCarb,
                    fat = lFat,
                    ingredients = listOf("Grilled Chicken Tenderloins (160g)", "Cooked Quinoa", "Spiced Chickpeas", "Cucumbers", "Feta & Olive Oil"),
                    preparation = "Toss warm chicken slices with tender quinoa, crunchy chickpeas, diced cucumber, and a light olive oil feta dressing.",
                    dietaryTag = "Both Veg & Non-Veg",
                    imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=500"
                ),
                DietPlanMeal(
                    mealType = "Snack",
                    title = "Greek Yogurt Parfait with Chia Seeds & Dark Chocolate",
                    portion = "220g",
                    calories = sCals,
                    protein = sProt,
                    carbs = sCarb,
                    fat = sFat,
                    ingredients = listOf("0% Greek Yogurt", "Chia Seeds", "Blueberries", "85% Dark Chocolate Shavings", "Raw Honey"),
                    preparation = "Fold chia seeds into chilled yogurt, top with antioxidant-rich blueberries and dark chocolate.",
                    dietaryTag = "Both Veg & Non-Veg",
                    imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=500"
                ),
                DietPlanMeal(
                    mealType = "Dinner",
                    title = "Pan-Seared Tofu & Edamame Coconut Curry with Brown Rice",
                    portion = "360g",
                    calories = dCals,
                    protein = dProt,
                    carbs = dCarb,
                    fat = dFat,
                    ingredients = listOf("Organic Extra Firm Tofu (180g)", "Edamame Beans", "Light Coconut Milk", "Brown Rice", "Thai Red Curry Paste"),
                    preparation = "Simmer crispy seared tofu cubes and edamame in fragrant light coconut curry broth. Serve hot over steaming brown rice.",
                    dietaryTag = "Both Veg & Non-Veg",
                    imageUrl = "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=500"
                )
            )
        }
    }
}
