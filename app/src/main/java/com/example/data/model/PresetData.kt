package com.example.data.model

object PresetData {

    val sampleScanFoods = listOf(
        FoodScanResult(
            name = "Panna Cotta",
            calories = 260,
            protein = 12,
            carbs = 24,
            fat = 20,
            portionGrams = 140,
            description = "Chocolate panna cotta is a smooth and creamy Italian dessert made with cream, sugar, and rich dark chocolate topped with fresh red cherries.",
            ingredients = listOf("Dark Chocolate", "Whole Milk", "Cherries", "Heavy Cream"),
            micronutrients = "Calcium: 15% DV, Iron: 4% DV, Sodium: 65mg",
            imageUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=600"
        ),
        FoodScanResult(
            name = "Quinoa Veggie Bowl",
            calories = 750,
            protein = 28,
            carbs = 88,
            fat = 22,
            portionGrams = 350,
            description = "Nutrient-dense warm quinoa bowl packed with roasted chickpeas, sliced avocado, steamed broccoli florets, and tahini drizzle.",
            ingredients = listOf("Tri-color Quinoa", "Hass Avocado", "Chickpeas", "Broccoli", "Tahini"),
            micronutrients = "Potassium: 820mg, Iron: 32% DV, Magnesium: 45% DV",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600"
        ),
        FoodScanResult(
            name = "Grilled Salmon Salad",
            calories = 520,
            protein = 44,
            carbs = 14,
            fat = 26,
            portionGrams = 280,
            description = "Wild Atlantic salmon fillet seared with herbs over baby spinach, cherry tomatoes, kalamata olives, and virgin olive oil.",
            ingredients = listOf("Wild Salmon", "Baby Spinach", "Cherry Tomatoes", "Extra Virgin Olive Oil"),
            micronutrients = "Omega-3: 2.4g, Vitamin D: 90% DV, Vitamin B12: 120% DV",
            imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600"
        ),
        FoodScanResult(
            name = "Açaí Superfood Bowl",
            calories = 380,
            protein = 11,
            carbs = 62,
            fat = 14,
            portionGrams = 260,
            description = "Pure organic açaí berry purée topped with sliced bananas, strawberries, chia seeds, and raw almond butter.",
            ingredients = listOf("Organic Açaí", "Fresh Banana", "Strawberries", "Chia Seeds", "Almond Butter"),
            micronutrients = "Antioxidants: High, Vitamin C: 45% DV, Fiber: 12g",
            imageUrl = "https://images.unsplash.com/photo-1590301157890-4810ed352733?w=600"
        ),
        FoodScanResult(
            name = "Grilled Chicken & Rice",
            calories = 580,
            protein = 52,
            carbs = 64,
            fat = 11,
            portionGrams = 320,
            description = "Lean grilled chicken breast served with steamed basmati jasmine rice, grilled asparagus, and sesame soy reduction.",
            ingredients = listOf("Chicken Breast", "Basmati Rice", "Asparagus", "Sesame Oil", "Sea Salt"),
            micronutrients = "Niacin: 85% DV, Zinc: 22% DV, Selenium: 60% DV",
            imageUrl = "https://images.unsplash.com/photo-1532550907401-a500c9a57435?w=600"
        )
    )

    val recipes = listOf(
        Recipe(
            id = "rec-1",
            title = "Quinoa Veggie Bowl",
            category = "Vegan",
            timeMinutes = 45,
            calories = 750,
            protein = 28,
            carbs = 88,
            fat = 22,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600",
            description = "A wholesome high-fiber bowl loaded with plant protein, healthy fats, and crisp vegetables.",
            ingredients = listOf("1 cup Quinoa", "1 Avocado", "1 cup Chickpeas", "1 cup Broccoli", "2 tbsp Tahini")
        ),
        Recipe(
            id = "rec-2",
            title = "Dark Chocolate Panna Cotta",
            category = "Snacks",
            timeMinutes = 30,
            calories = 260,
            protein = 12,
            carbs = 24,
            fat = 20,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=600",
            description = "Velvety dark chocolate Italian dessert topped with fresh tart cherries and cocoa nibs.",
            ingredients = listOf("70% Dark Chocolate", "Heavy Cream", "Gelatin", "Fresh Cherries", "Vanilla Extract")
        ),
        Recipe(
            id = "rec-3",
            title = "Seared Salmon & Greens",
            category = "Protein",
            timeMinutes = 20,
            calories = 520,
            protein = 44,
            carbs = 14,
            fat = 26,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600",
            description = "Crispy skin salmon with wilted garlic spinach, roasted lemon, and avocado oil.",
            ingredients = listOf("200g Wild Salmon", "3 cups Spinach", "1 Lemon", "1 tbsp Olive Oil", "Sea Salt")
        ),
        Recipe(
            id = "rec-4",
            title = "Greek Yogurt Protein Parfait",
            category = "Snacks",
            timeMinutes = 5,
            calories = 280,
            protein = 24,
            carbs = 32,
            fat = 4,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1488477181946-6428a0291777?w=600",
            description = "High-protein snack with rich probiotics, fresh blueberries, and raw wildflower honey.",
            ingredients = listOf("250g Greek Yogurt 0%", "1/2 cup Blueberries", "1 tbsp Honey", "1 tbsp Chia Seeds")
        ),
        Recipe(
            id = "rec-5",
            title = "Spiced Tofu Buddha Bowl",
            category = "Vegan",
            timeMinutes = 25,
            calories = 490,
            protein = 32,
            carbs = 55,
            fat = 15,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600",
            description = "Crispy pan-seared organic tofu cubes with brown rice, edamame, and peanut ginger dressing.",
            ingredients = listOf("200g Extra Firm Tofu", "1 cup Brown Rice", "1/2 cup Edamame", "Peanut Dressing")
        ),
        Recipe(
            id = "rec-6",
            title = "Steak & Sweet Potato Mash",
            category = "Protein",
            timeMinutes = 35,
            calories = 680,
            protein = 54,
            carbs = 58,
            fat = 18,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1544025162-d76694265947?w=600",
            description = "Prime grass-fed sirloin steak with roasted sweet potato mash and sautéed green beans.",
            ingredients = listOf("220g Sirloin Steak", "1 Large Sweet Potato", "Green Beans", "Grass-fed Butter")
        )
    )

    val bulkWorkouts = listOf(
        WorkoutRoutine(
            id = "bulk-push",
            title = "Upper Body Hypertrophy (Push)",
            goalType = "BULK",
            durationMinutes = 45,
            level = "Intermediate",
            description = "Focus on chest, shoulder delts, and triceps mechanical tension for lean muscle growth.",
            estimatedCalories = 380,
            exercises = listOf(
                Exercise(
                    id = "ex-1",
                    name = "Barbell Incline Bench Press",
                    targetMuscle = "Upper Pectorals",
                    sets = 4,
                    reps = "8-10 reps",
                    restSeconds = 90,
                    caloriesBurnEstimate = 95,
                    formInstructions = listOf(
                        "Set bench angle to 30 degrees.",
                        "Retract scapulae and plant feet firmly into the floor.",
                        "Lower bar controlled to upper chest, then press explosively upward."
                    ),
                    tips = "Do not flare elbows beyond 75 degrees to protect anterior rotator cuffs."
                ),
                Exercise(
                    id = "ex-2",
                    name = "Dumbbell Overhead Shoulder Press",
                    targetMuscle = "Anterior & Lateral Deltoids",
                    sets = 4,
                    reps = "10-12 reps",
                    restSeconds = 75,
                    caloriesBurnEstimate = 85,
                    formInstructions = listOf(
                        "Sit upright with back supported.",
                        "Press dumbbells directly upward until arms are straight overhead.",
                        "Lower under 3-second control until dumbbells touch ear level."
                    ),
                    tips = "Keep core braced tightly to prevent lumbar hyperextension."
                ),
                Exercise(
                    id = "ex-3",
                    name = "Triceps Cable Rope Pushdowns",
                    targetMuscle = "Triceps Lateral & Medial Heads",
                    sets = 3,
                    reps = "12-15 reps",
                    restSeconds = 60,
                    caloriesBurnEstimate = 60,
                    formInstructions = listOf(
                        "Pin elbows directly to sides of ribs.",
                        "Extend forearms down and flare the rope outwards at bottom peak contraction.",
                        "Squeeze triceps for 1 full second."
                    ),
                    tips = "Avoid using upper body momentum to swing the weight down."
                )
            )
        ),
        WorkoutRoutine(
            id = "bulk-legs",
            title = "Lower Body Hypertrophy (Squat & Chain)",
            goalType = "BULK",
            durationMinutes = 50,
            level = "Advanced",
            description = "High-volume quadriceps and posterior chain loading for mass.",
            estimatedCalories = 450,
            exercises = listOf(
                Exercise(
                    id = "ex-4",
                    name = "Barbell Back Squats",
                    targetMuscle = "Quadriceps & Glutes",
                    sets = 4,
                    reps = "6-8 reps",
                    restSeconds = 120,
                    caloriesBurnEstimate = 140,
                    formInstructions = listOf(
                        "Rest bar comfortably across upper traps.",
                        "Inhale deep into diaphragm, brace core 360 degrees.",
                        "Hinge hips back and descend until hip crease is below knee line.",
                        "Drive up through the mid-foot."
                    ),
                    tips = "Maintain a neutral cervical and thoracic spine throughout."
                ),
                Exercise(
                    id = "ex-5",
                    name = "Romanian Deadlifts (RDL)",
                    targetMuscle = "Hamstrings & Gluteus Maximus",
                    sets = 4,
                    reps = "8-10 reps",
                    restSeconds = 90,
                    caloriesBurnEstimate = 110,
                    formInstructions = listOf(
                        "Hold dumbbells or barbell with slight knee flexion.",
                        "Push hips back toward wall as if closing a car door with hips.",
                        "Lower until hamstrings reach full stretch, then contract glutes to return."
                    ),
                    tips = "Keep bar glued close to thighs and shins."
                )
            )
        )
    )

    val cutWorkouts = listOf(
        WorkoutRoutine(
            id = "cut-hiit",
            title = "Metabolic HIIT & Core Fat Burn",
            goalType = "CUT",
            durationMinutes = 30,
            level = "High Energy",
            description = "High intensity interval protocol maximizing excess post-exercise oxygen consumption (EPOC).",
            estimatedCalories = 350,
            exercises = listOf(
                Exercise(
                    id = "ex-6",
                    name = "Kettlebell Swing Intervals",
                    targetMuscle = "Posterior Chain & Cardiovascular",
                    sets = 5,
                    reps = "45 sec on / 15 sec off",
                    restSeconds = 45,
                    caloriesBurnEstimate = 120,
                    formInstructions = listOf(
                        "Hinge at the hips, keeping back straight.",
                        "Snap hips violently forward to drive kettlebell to chest height.",
                        "Let gravity guide the bell back down into the hip hinge."
                    ),
                    tips = "Power comes entirely from the hips and glutes, not the arms."
                ),
                Exercise(
                    id = "ex-7",
                    name = "Bodyweight Burpees & Jump",
                    targetMuscle = "Full Body Plyometric",
                    sets = 4,
                    reps = "40 sec work",
                    restSeconds = 30,
                    caloriesBurnEstimate = 100,
                    formInstructions = listOf(
                        "Drop chest completely to the floor.",
                        "Snap feet forward and jump up with hands overhead.",
                        "Land softly with flexed knees."
                    ),
                    tips = "Pace yourself steadily to maintain cadence across all intervals."
                ),
                Exercise(
                    id = "ex-8",
                    name = "Hanging Knee Raises & Plank Hold",
                    targetMuscle = "Rectus Abdominis & Obliques",
                    sets = 3,
                    reps = "15 reps + 45s hold",
                    restSeconds = 45,
                    caloriesBurnEstimate = 65,
                    formInstructions = listOf(
                        "Hang from pull-up bar without swinging.",
                        "Curl knees toward sternum using lower abdominal contraction.",
                        "Transition immediately into a hollow-body forearm plank."
                    ),
                    tips = "Do not use momentum or kick legs back."
                )
            )
        ),
        WorkoutRoutine(
            id = "cut-density",
            title = "Full Body Circuit Density",
            goalType = "CUT",
            durationMinutes = 35,
            level = "Intermediate",
            description = "High density compound supersets preserving lean muscle while maximizing caloric expenditure.",
            estimatedCalories = 390,
            exercises = listOf(
                Exercise(
                    id = "ex-9",
                    name = "Dumbbell Goblet Squat",
                    targetMuscle = "Quadriceps & Core",
                    sets = 4,
                    reps = "15 reps",
                    restSeconds = 30,
                    caloriesBurnEstimate = 95,
                    formInstructions = listOf(
                        "Hold heavy dumbbell vertically against sternum.",
                        "Squat deep between knees with elbows tracking inside thighs.",
                        "Drive up quickly to maintain continuous tempo."
                    ),
                    tips = "Keep chest tall and upright."
                ),
                Exercise(
                    id = "ex-10",
                    name = "Push-Up to Renegade Row",
                    targetMuscle = "Chest, Lats & Anti-Rotation Core",
                    sets = 4,
                    reps = "12 reps",
                    restSeconds = 45,
                    caloriesBurnEstimate = 85,
                    formInstructions = listOf(
                        "Perform a strict push-up on dumbbells.",
                        "Row one dumbbell up to hip without letting hips rotate.",
                        "Alternate sides every repetition."
                    ),
                    tips = "Widen feet stance slightly for stability."
                )
            )
        )
    )
}
