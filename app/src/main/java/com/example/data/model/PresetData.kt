package com.example.data.model

object PresetData {

    val sampleScanFoods = listOf(
        // INDIAN CUISINE (🇮🇳)
        FoodScanResult(
            name = "Butter Chicken & Naan",
            calories = 590,
            protein = 46,
            carbs = 48,
            fat = 24,
            portionGrams = 320,
            description = "Tender marinated chicken pieces simmered in a velvety spiced tomato, butter, and fenugreek gravy paired with warm garlic naan.",
            ingredients = listOf("Chicken Breast", "Tomato Purée", "Dairy Butter", "Garam Masala", "Kasuri Methi", "Garlic Naan"),
            micronutrients = "Iron: 22% DV, Vitamin A: 35% DV, Calcium: 14% DV",
            imageUrl = "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=600",
            dietaryTag = "Non-Vegetarian",
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        FoodScanResult(
            name = "Paneer Tikka Platter",
            calories = 380,
            protein = 24,
            carbs = 16,
            fat = 26,
            portionGrams = 240,
            description = "Cubed cottage cheese marinated in yogurt and tandoori spices, char-grilled with crunchy bell peppers, onions, and mint chutney.",
            ingredients = listOf("Fresh Paneer", "Hung Curd", "Tandoori Masala", "Bell Peppers", "Mint-Coriander Chutney"),
            micronutrients = "Calcium: 48% DV, Phosphorus: 30% DV, Vitamin B12: 25% DV",
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600",
            dietaryTag = "Vegetarian",
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        FoodScanResult(
            name = "Masala Dosa & Sambar",
            calories = 410,
            protein = 12,
            carbs = 72,
            fat = 10,
            portionGrams = 280,
            description = "Crispy golden fermented rice-lentil crepe stuffed with aromatic mustard-tempered potato masala, served with tangy vegetable sambar.",
            ingredients = listOf("Rice & Urad Dal", "Potatoes", "Mustard Seeds", "Curry Leaves", "Toor Dal Sambar", "Coconut Chutney"),
            micronutrients = "Fiber: 8g, Potassium: 520mg, Iron: 18% DV",
            imageUrl = "https://images.unsplash.com/photo-1668236543090-82eba5ee5976?w=600",
            dietaryTag = "Vegan",
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        FoodScanResult(
            name = "Chicken Dum Biryani",
            calories = 660,
            protein = 44,
            carbs = 76,
            fat = 20,
            portionGrams = 380,
            description = "Royal Hyderabadi basmati rice cooked on slow steam with saffron, caramelized onions, fresh mint, and tender spiced chicken.",
            ingredients = listOf("Aged Basmati Rice", "Bone-in Chicken", "Saffron Milk", "Fried Onions", "Whole Cardamom & Cloves"),
            micronutrients = "Niacin: 60% DV, Selenium: 45% DV, Zinc: 28% DV",
            imageUrl = "https://images.unsplash.com/photo-1563379091339-03b21ab4a4f8?w=600",
            dietaryTag = "Non-Vegetarian",
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        FoodScanResult(
            name = "Dal Makhani & Jeera Rice",
            calories = 480,
            protein = 18,
            carbs = 68,
            fat = 16,
            portionGrams = 340,
            description = "Slow-simmered whole black urad lentils cooked overnight with ginger, tomatoes, and a touch of butter over cumin basmati rice.",
            ingredients = listOf("Black Urad Lentils", "Kidney Beans", "Cumin Seeds", "Basmati Rice", "Ginger Garlic Paste"),
            micronutrients = "Fiber: 14g, Iron: 36% DV, Magnesium: 28% DV",
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
            dietaryTag = "Vegetarian",
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),

        // FRENCH CUISINE (🇫🇷)
        FoodScanResult(
            name = "Boeuf Bourguignon",
            calories = 570,
            protein = 50,
            carbs = 18,
            fat = 32,
            portionGrams = 340,
            description = "Classic French slow-braised tender beef chuck stewed in Burgundy red wine, baby carrots, pearl onions, and sautéed garlic mushrooms.",
            ingredients = listOf("Prime Beef Chuck", "French Red Wine", "Smoked Bacon Lardons", "Carrots", "Button Mushrooms", "Fresh Thyme"),
            micronutrients = "Iron: 42% DV, Zinc: 65% DV, Vitamin B12: 150% DV",
            imageUrl = "https://images.unsplash.com/photo-1534939561126-855b8675edd7?w=600",
            dietaryTag = "High-Protein",
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),
        FoodScanResult(
            name = "Ratatouille Provençale",
            calories = 230,
            protein = 7,
            carbs = 26,
            fat = 12,
            portionGrams = 300,
            description = "Layered French Riviera vegetable medley of zucchini, eggplant, ripe bell peppers, and San Marzano tomatoes seasoned with herbs de Provence.",
            ingredients = listOf("Zucchini", "Eggplant", "Roma Tomatoes", "Yellow Squash", "Herbes de Provence", "Extra Virgin Olive Oil"),
            micronutrients = "Vitamin C: 85% DV, Potassium: 610mg, Fiber: 9g",
            imageUrl = "https://images.unsplash.com/photo-1572453800999-e8d2d1589b7c?w=600",
            dietaryTag = "Vegan",
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),
        FoodScanResult(
            name = "Coq au Vin Traditionnel",
            calories = 510,
            protein = 46,
            carbs = 14,
            fat = 28,
            portionGrams = 320,
            description = "Rustic French country chicken braised in vintage pinot noir with bacon lardons, shallots, garlic, and fresh rosemary sprigs.",
            ingredients = listOf("Free-Range Chicken", "Pinot Noir", "Pancetta Lardons", "Shallots", "Cremini Mushrooms", "Bay Leaves"),
            micronutrients = "Selenium: 55% DV, Niacin: 70% DV, Vitamin B6: 45% DV",
            imageUrl = "https://images.unsplash.com/photo-1600891964599-f61ba0e24092?w=600",
            dietaryTag = "High-Protein",
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),
        FoodScanResult(
            name = "Salade Niçoise au Thon",
            calories = 430,
            protein = 36,
            carbs = 18,
            fat = 24,
            portionGrams = 290,
            description = "Mediterranean French salad with pan-seared yellowfin tuna, tender haricots verts, soft-boiled organic egg, and Niçoise olives.",
            ingredients = listOf("Yellowfin Tuna", "French Green Beans", "Soft-boiled Egg", "Kalamata Olives", "Dijon Vinaigrette"),
            micronutrients = "Omega-3: 1.8g, Vitamin D: 75% DV, Vitamin K: 110% DV",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600",
            dietaryTag = "Pescatarian",
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),
        FoodScanResult(
            name = "Croissant & Normandie Brie",
            calories = 360,
            protein = 11,
            carbs = 34,
            fat = 21,
            portionGrams = 150,
            description = "Flaky Parisian laminated butter pastry paired with ripe Normandie Brie cheese, fresh black figs, and raw honey drizzle.",
            ingredients = listOf("Laminated Butter Pastry", "Normandie Brie", "Fresh Mission Figs", "Thyme Honey"),
            micronutrients = "Calcium: 24% DV, Vitamin A: 20% DV, Riboflavin: 18% DV",
            imageUrl = "https://images.unsplash.com/photo-1555507036-ab1f4038808a?w=600",
            dietaryTag = "Vegetarian",
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),

        // JAPANESE CUISINE (🇯🇵)
        FoodScanResult(
            name = "Tonkotsu Chashu Ramen",
            calories = 690,
            protein = 38,
            carbs = 82,
            fat = 26,
            portionGrams = 460,
            description = "Rich 18-hour simmered pork collagen broth with wheat noodles, torch-seared pork belly chashu, seasoned ajitsuke egg, and roasted nori.",
            ingredients = listOf("Ramen Noodles", "Pork Broth", "Chashu Pork Belly", "Nitago Soft Egg", "Bamboo Shoots", "Scallions", "Nori"),
            micronutrients = "Collagen: High, Iron: 24% DV, Sodium: Controlled",
            imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600",
            dietaryTag = "Non-Vegetarian",
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),
        FoodScanResult(
            name = "Salmon & Tuna Sashimi Nigiri",
            calories = 430,
            protein = 40,
            carbs = 46,
            fat = 8,
            portionGrams = 250,
            description = "Artisanal assortment of fresh wild salmon, ahi tuna, and avocado over seasoned sushi rice with real wasabi root and pickled gari.",
            ingredients = listOf("Atlantic Salmon", "Yellowfin Tuna", "Seasoned Sushi Rice", "Avocado", "Fresh Wasabi", "Pickled Ginger"),
            micronutrients = "Omega-3: 2.6g, Vitamin D: 95% DV, Iodine: 140% DV",
            imageUrl = "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),
        FoodScanResult(
            name = "Chicken Teriyaki Bento Bowl",
            calories = 540,
            protein = 44,
            carbs = 62,
            fat = 12,
            portionGrams = 340,
            description = "Glazed grilled chicken thigh with authentic mirin-soy reduction over premium Koshihikari rice, steamed edamame, and pickled radish.",
            ingredients = listOf("Chicken Thigh", "Teriyaki Sauce (Mirin, Tamari)", "Koshihikari Rice", "Edamame", "Sesame Seeds"),
            micronutrients = "Phosphorus: 38% DV, Vitamin B6: 55% DV, Potassium: 640mg",
            imageUrl = "https://images.unsplash.com/photo-1532550907401-a500c9a57435?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),
        FoodScanResult(
            name = "Matcha Cold Soba & Tempura",
            calories = 470,
            protein = 18,
            carbs = 74,
            fat = 12,
            portionGrams = 290,
            description = "Chilled organic green tea buckwheat soba noodles served with dashi-tsuyu dipping sauce, wasabi, and crispy tiger prawn tempura.",
            ingredients = listOf("Buckwheat Matcha Soba", "Kombu Dashi Dip", "Tiger Prawn", "Shiitake Mushroom", "Daikon Radish"),
            micronutrients = "Rutin (Antioxidant): Very High, Magnesium: 32% DV",
            imageUrl = "https://images.unsplash.com/photo-1618841557871-b4664fbf0cb3?w=600",
            dietaryTag = "Pescatarian",
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),

        // ITALIAN CUISINE (🇮🇹)
        FoodScanResult(
            name = "Pizza Margherita Napoletana",
            calories = 650,
            protein = 28,
            carbs = 84,
            fat = 22,
            portionGrams = 320,
            description = "Authentic wood-fired Neapolitan sourdough crust topped with sweet San Marzano tomato sauce, fresh buffalo mozzarella, and basil.",
            ingredients = listOf("00 Flour Sourdough", "San Marzano D.O.P. Tomatoes", "Mozzarella di Bufala", "Fresh Basil", "Extra Virgin Olive Oil"),
            micronutrients = "Calcium: 42% DV, Lycopene: Very High, Phosphorus: 34% DV",
            imageUrl = "https://images.unsplash.com/photo-1513104890138-7c749659a591?w=600",
            dietaryTag = "Vegetarian",
            cuisine = "Italian",
            countryFlag = "🇮🇹"
        ),
        FoodScanResult(
            name = "Spaghetti alla Carbonara",
            calories = 590,
            protein = 30,
            carbs = 65,
            fat = 24,
            portionGrams = 280,
            description = "Traditional Roman recipe prepared with crispy cured guanciale, pasture-raised egg yolks, freshly grated Pecorino Romano, and black pepper.",
            ingredients = listOf("Bronze-die Spaghetti", "Cured Guanciale", "Egg Yolks", "Pecorino Romano", "Tellicherry Black Pepper"),
            micronutrients = "Vitamin B12: 45% DV, Zinc: 22% DV, Choline: 35% DV",
            imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=600",
            dietaryTag = "Non-Vegetarian",
            cuisine = "Italian",
            countryFlag = "🇮🇹"
        ),
        FoodScanResult(
            name = "Panna Cotta al Cioccolato",
            calories = 260,
            protein = 12,
            carbs = 24,
            fat = 20,
            portionGrams = 140,
            description = "Silky smooth Italian sweet cream dessert infused with 70% dark cocoa and topped with fresh wild sour cherries.",
            ingredients = listOf("Whole Dairy Cream", "70% Dark Cocoa", "Gelatin", "Fresh Cherries", "Vanilla Bean"),
            micronutrients = "Calcium: 16% DV, Iron: 6% DV, Magnesium: 15% DV",
            imageUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=600",
            dietaryTag = "Vegetarian",
            cuisine = "Italian",
            countryFlag = "🇮🇹"
        ),

        // MEXICAN CUISINE (🇲🇽)
        FoodScanResult(
            name = "Street Tacos al Pastor",
            calories = 460,
            protein = 36,
            carbs = 42,
            fat = 16,
            portionGrams = 250,
            description = "Tender pork marinated in achiote and guajillo chilies, seared with caramelized pineapple, diced white onions, and cilantro on corn tortillas.",
            ingredients = listOf("Marinated Pork Loin", "Corn Tortillas", "Roasted Pineapple", "Cilantro", "White Onion", "Salsa Verde"),
            micronutrients = "Thiamin: 48% DV, Vitamin C: 32% DV, Zinc: 28% DV",
            imageUrl = "https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Mexican",
            countryFlag = "🇲🇽"
        ),
        FoodScanResult(
            name = "Guacamole & Tortilla Bowl",
            calories = 340,
            protein = 6,
            carbs = 32,
            fat = 22,
            portionGrams = 210,
            description = "Hand-mashed Hass avocados with freshly squeezed lime juice, jalapeños, diced Roma tomatoes, cilantro, and crispy baked corn chips.",
            ingredients = listOf("Hass Avocados", "Fresh Lime Juice", "Jalapeño", "Cilantro", "Baked Tortilla Chips"),
            micronutrients = "Monounsaturated Fat: 16g, Potassium: 680mg, Fiber: 11g",
            imageUrl = "https://images.unsplash.com/photo-1541288097308-7b8e3f58c4c6?w=600",
            dietaryTag = "Vegan",
            cuisine = "Mexican",
            countryFlag = "🇲🇽"
        ),

        // THAI & SOUTHEAST ASIAN (🇹🇭 / 🇻🇳)
        FoodScanResult(
            name = "Pad Thai with Tiger Prawns",
            calories = 570,
            protein = 34,
            carbs = 76,
            fat = 15,
            portionGrams = 340,
            description = "Stir-fried Thai rice noodles with succulent tiger prawns, firm tofu, bean sprouts, garlic chives, crushed peanuts, and tamarind glaze.",
            ingredients = listOf("Rice Noodles", "Tiger Prawns", "Tofu", "Tamarind Pulp", "Peanuts", "Bean Sprouts", "Lime"),
            micronutrients = "Zinc: 34% DV, Iron: 20% DV, Vitamin C: 25% DV",
            imageUrl = "https://images.unsplash.com/photo-1559847844-5315695dadae?w=600",
            dietaryTag = "Pescatarian",
            cuisine = "Thai",
            countryFlag = "🇹🇭"
        ),
        FoodScanResult(
            name = "Vietnamese Beef Pho (Phở Bò)",
            calories = 450,
            protein = 38,
            carbs = 58,
            fat = 8,
            portionGrams = 460,
            description = "12-hour simmered beef bone broth infused with star anise and cinnamon, poured over thin rice noodles, rare ribeye slices, and Thai basil.",
            ingredients = listOf("Beef Bone Broth", "Ribeye Beef Slices", "Rice Pho Noodles", "Star Anise", "Thai Basil", "Bean Sprouts"),
            micronutrients = "Iron: 38% DV, Zinc: 45% DV, Collagen: High",
            imageUrl = "https://images.unsplash.com/photo-1582878826629-29b7ad1cdc43?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Thai",
            countryFlag = "🇻🇳"
        ),

        // MEDITERRANEAN CUISINE (🇬🇷 / 🇱🇧)
        FoodScanResult(
            name = "Greek Souvlaki & Tzatziki",
            calories = 490,
            protein = 44,
            carbs = 36,
            fat = 18,
            portionGrams = 300,
            description = "Char-grilled oregano chicken breast skewers served with cooling Greek yogurt-cucumber tzatziki, warm pita, and tomato-onion salad.",
            ingredients = listOf("Chicken Breast", "Greek Yogurt", "English Cucumber", "Garlic", "Oregano", "Whole Wheat Pita"),
            micronutrients = "Protein: 44g, Calcium: 22% DV, Vitamin B6: 60% DV",
            imageUrl = "https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Mediterranean",
            countryFlag = "🇬🇷"
        ),
        FoodScanResult(
            name = "Falafel & Tahini Mezze",
            calories = 460,
            protein = 18,
            carbs = 56,
            fat = 19,
            portionGrams = 290,
            description = "Crispy golden spiced chickpea falafels served with smooth sesame tahini, parsley tabouleh, pickled turnips, and creamy hummus.",
            ingredients = listOf("Soaked Chickpeas", "Fresh Parsley & Mint", "Sesame Tahini", "Garlic", "Coriander", "Cumin"),
            micronutrients = "Fiber: 12g, Iron: 32% DV, Magnesium: 35% DV",
            imageUrl = "https://images.unsplash.com/photo-1593560708920-61dd98c46a4e?w=600",
            dietaryTag = "Vegan",
            cuisine = "Mediterranean",
            countryFlag = "🇱🇧"
        ),

        // GLOBAL SUPERFOOD FITNESS
        FoodScanResult(
            name = "Quinoa Veggie Power Bowl",
            calories = 750,
            protein = 28,
            carbs = 88,
            fat = 22,
            portionGrams = 350,
            description = "Nutrient-dense warm quinoa bowl packed with roasted chickpeas, sliced avocado, steamed broccoli florets, and tahini drizzle.",
            ingredients = listOf("Tri-color Quinoa", "Hass Avocado", "Chickpeas", "Broccoli", "Tahini"),
            micronutrients = "Potassium: 820mg, Iron: 32% DV, Magnesium: 45% DV",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600",
            dietaryTag = "Vegan",
            cuisine = "Global",
            countryFlag = "🥗"
        ),
        FoodScanResult(
            name = "Grilled Atlantic Salmon Salad",
            calories = 520,
            protein = 44,
            carbs = 14,
            fat = 26,
            portionGrams = 280,
            description = "Wild Atlantic salmon fillet seared with herbs over baby spinach, cherry tomatoes, kalamata olives, and virgin olive oil.",
            ingredients = listOf("Wild Salmon", "Baby Spinach", "Cherry Tomatoes", "Extra Virgin Olive Oil"),
            micronutrients = "Omega-3: 2.4g, Vitamin D: 90% DV, Vitamin B12: 120% DV",
            imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600",
            dietaryTag = "High-Protein",
            cuisine = "Global",
            countryFlag = "🐟"
        ),
        FoodScanResult(
            name = "Açaí Superfood Berry Bowl",
            calories = 380,
            protein = 11,
            carbs = 62,
            fat = 14,
            portionGrams = 260,
            description = "Pure organic açaí berry purée topped with sliced bananas, strawberries, chia seeds, and raw almond butter.",
            ingredients = listOf("Organic Açaí", "Fresh Banana", "Strawberries", "Chia Seeds", "Almond Butter"),
            micronutrients = "Antioxidants: High, Vitamin C: 45% DV, Fiber: 12g",
            imageUrl = "https://images.unsplash.com/photo-1590301157890-4810ed352733?w=600",
            dietaryTag = "Vegan",
            cuisine = "Global",
            countryFlag = "🍓"
        )
    )

    val recipes = listOf(
        // INDIAN RECIPES
        Recipe(
            id = "rec-in-1",
            title = "Authentic Murgh Makhani (Butter Chicken)",
            category = "Indian",
            timeMinutes = 35,
            calories = 590,
            protein = 46,
            carbs = 48,
            fat = 24,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1588166524941-3bf61a9c41db?w=600",
            description = "Aromatic high-protein Indian chicken in a silky tomato-cashew curry infused with fenugreek and butter.",
            ingredients = listOf("500g Chicken Breast", "2 cups Tomato Puree", "2 tbsp Cashew Paste", "2 tbsp Butter", "1 tbsp Kasuri Methi", "Garam Masala"),
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        Recipe(
            id = "rec-in-2",
            title = "Tandoori Paneer Tikka",
            category = "Indian",
            timeMinutes = 25,
            calories = 380,
            protein = 24,
            carbs = 16,
            fat = 26,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1567188040759-fb8a883dc6d8?w=600",
            description = "Char-grilled spiced cottage cheese skewers packed with calcium and clean vegetarian protein.",
            ingredients = listOf("250g Paneer", "1/2 cup Greek Yogurt", "1 tbsp Kashmiri Chili", "Bell Peppers & Red Onions", "Chaat Masala"),
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),
        Recipe(
            id = "rec-in-3",
            title = "Overnight Slow Dal Makhani",
            category = "Indian",
            timeMinutes = 45,
            calories = 480,
            protein = 18,
            carbs = 68,
            fat = 16,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1546833999-b9f581a1996d?w=600",
            description = "Rich, slow-simmered whole black lentils with ginger and cumin, providing abundant iron and plant fiber.",
            ingredients = listOf("1 cup Black Urad Dal", "1/4 cup Kidney Beans", "1 tbsp Ginger Garlic", "1 cup Tomato Puree", "1 tbsp Butter"),
            cuisine = "Indian",
            countryFlag = "🇮🇳"
        ),

        // FRENCH RECIPES
        Recipe(
            id = "rec-fr-1",
            title = "Boeuf Bourguignon Classique",
            category = "French",
            timeMinutes = 60,
            calories = 570,
            protein = 50,
            carbs = 18,
            fat = 32,
            difficulty = "Hard",
            imageUrl = "https://images.unsplash.com/photo-1534939561126-855b8675edd7?w=600",
            description = "Grand French cuisine: beef chuck slow-braised in red wine broth with herbs and mushrooms.",
            ingredients = listOf("600g Lean Beef Chuck", "2 cups Red Wine", "100g Lardons", "2 Carrots", "150g Mushrooms", "Fresh Thyme"),
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),
        Recipe(
            id = "rec-fr-2",
            title = "Ratatouille Niçoise",
            category = "French",
            timeMinutes = 35,
            calories = 230,
            protein = 7,
            carbs = 26,
            fat = 12,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1572453800999-e8d2d1589b7c?w=600",
            description = "Warm layered Provençal vegetable bake bursting with antioxidants, lycopene, and vitamin C.",
            ingredients = listOf("1 Eggplant", "2 Zucchinis", "4 Roma Tomatoes", "1 Bell Pepper", "Herbes de Provence", "Olive Oil"),
            cuisine = "French",
            countryFlag = "🇫🇷"
        ),

        // JAPANESE RECIPES
        Recipe(
            id = "rec-jp-1",
            title = "Artisanal Salmon & Ahi Poke Bowl",
            category = "Japanese",
            timeMinutes = 15,
            calories = 430,
            protein = 40,
            carbs = 46,
            fat = 8,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1579871494447-9811cf80d66c?w=600",
            description = "Fresh sashimi-grade salmon and yellowfin tuna over seasoned sushi rice with nori and sesame.",
            ingredients = listOf("200g Fresh Salmon Fillet", "100g Ahi Tuna", "1 cup Sushi Rice", "Edamame", "Tamari Soy Sauce", "Nori"),
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),
        Recipe(
            id = "rec-jp-2",
            title = "Tokyo Tonkotsu Chashu Ramen",
            category = "Japanese",
            timeMinutes = 50,
            calories = 690,
            protein = 38,
            carbs = 82,
            fat = 26,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?w=600",
            description = "Rich umami-packed pork bone broth with springy noodles, chashu pork, and soft-boiled egg.",
            ingredients = listOf("Ramen Noodles", "Pork Broth", "Chashu Pork", "Nitago Marinated Egg", "Bamboo Shoots", "Green Onions"),
            cuisine = "Japanese",
            countryFlag = "🇯🇵"
        ),

        // ITALIAN RECIPES
        Recipe(
            id = "rec-it-1",
            title = "Authentic Spaghetti Carbonara",
            category = "Italian",
            timeMinutes = 20,
            calories = 590,
            protein = 30,
            carbs = 65,
            fat = 24,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1612874742237-6526221588e3?w=600",
            description = "Roman classic made with crispy guanciale, pasture egg yolks, and aged Pecorino Romano cheese.",
            ingredients = listOf("200g Spaghetti", "80g Guanciale or Pancetta", "2 Egg Yolks + 1 Whole Egg", "50g Pecorino Romano", "Black Pepper"),
            cuisine = "Italian",
            countryFlag = "🇮🇹"
        ),
        Recipe(
            id = "rec-it-2",
            title = "Dark Chocolate Panna Cotta",
            category = "Italian",
            timeMinutes = 30,
            calories = 260,
            protein = 12,
            carbs = 24,
            fat = 20,
            difficulty = "Medium",
            imageUrl = "https://images.unsplash.com/photo-1541781774459-bb2af2f05b55?w=600",
            description = "Velvety dark chocolate Italian dessert topped with fresh tart cherries and cocoa nibs.",
            ingredients = listOf("70% Dark Chocolate", "Heavy Cream", "Gelatin", "Fresh Cherries", "Vanilla Extract"),
            cuisine = "Italian",
            countryFlag = "🇮🇹"
        ),

        // MEXICAN RECIPES
        Recipe(
            id = "rec-mx-1",
            title = "Achiote Street Tacos al Pastor",
            category = "Mexican",
            timeMinutes = 30,
            calories = 460,
            protein = 36,
            carbs = 42,
            fat = 16,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1551504734-5ee1c4a1479b?w=600",
            description = "Zesty spiced pork tacos with grilled pineapple and fresh salsa verde.",
            ingredients = listOf("300g Lean Pork", "Achiote Paste", "Corn Tortillas", "Fresh Pineapple", "White Onion & Cilantro"),
            cuisine = "Mexican",
            countryFlag = "🇲🇽"
        ),

        // THAI & MEDITERRANEAN RECIPES
        Recipe(
            id = "rec-th-1",
            title = "Tiger Prawn Pad Thai",
            category = "Thai",
            timeMinutes = 25,
            calories = 570,
            protein = 34,
            carbs = 76,
            fat = 15,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1559847844-5315695dadae?w=600",
            description = "Street style stir-fried tamarind noodles with juicy tiger prawns, crushed peanuts, and lime.",
            ingredients = listOf("150g Rice Noodles", "200g Tiger Prawns", "50g Tofu", "Tamarind Paste", "Peanuts", "Bean Sprouts"),
            cuisine = "Thai",
            countryFlag = "🇹🇭"
        ),
        Recipe(
            id = "rec-med-1",
            title = "Greek Chicken Souvlaki & Tzatziki",
            category = "Mediterranean",
            timeMinutes = 25,
            calories = 490,
            protein = 44,
            carbs = 36,
            fat = 18,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1529193591184-b1d58069ecdd?w=600",
            description = "Marinated oregano chicken skewers with cooling yogurt dip and warm pita bread.",
            ingredients = listOf("350g Chicken Breast", "1 cup Greek Yogurt", "Cucumber", "Garlic", "Oregano & Olive Oil", "Whole Pita"),
            cuisine = "Mediterranean",
            countryFlag = "🇬🇷"
        ),
        Recipe(
            id = "rec-fit-1",
            title = "Quinoa Veggie Power Bowl",
            category = "Vegan",
            timeMinutes = 20,
            calories = 750,
            protein = 28,
            carbs = 88,
            fat = 22,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1540420773420-3366772f4999?w=600",
            description = "A wholesome high-fiber bowl loaded with plant protein, healthy fats, and crisp vegetables.",
            ingredients = listOf("1 cup Quinoa", "1 Avocado", "1 cup Chickpeas", "1 cup Broccoli", "2 tbsp Tahini"),
            cuisine = "Global",
            countryFlag = "🥗"
        ),
        Recipe(
            id = "rec-fit-2",
            title = "Wild Salmon & Garlic Spinach",
            category = "Protein",
            timeMinutes = 20,
            calories = 520,
            protein = 44,
            carbs = 14,
            fat = 26,
            difficulty = "Easy",
            imageUrl = "https://images.unsplash.com/photo-1467003909585-2f8a72700288?w=600",
            description = "Crispy skin salmon with wilted garlic spinach, roasted lemon, and virgin avocado oil.",
            ingredients = listOf("200g Wild Salmon", "3 cups Spinach", "1 Lemon", "1 tbsp Olive Oil", "Sea Salt"),
            cuisine = "Global",
            countryFlag = "🐟"
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

