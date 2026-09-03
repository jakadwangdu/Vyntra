package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.MealEntity
import com.example.data.local.UserProfileEntity
import com.example.data.local.WaterLogEntity
import com.example.data.local.WorkoutLogEntity
import com.example.data.model.ChatMessage
import com.example.data.model.CustomDietPlan
import com.example.data.model.DietPlanGenerator
import com.example.data.model.DietPlanMeal
import com.example.data.model.FoodScanResult
import com.example.data.model.MessageSender
import com.example.data.model.PresetData
import com.example.data.model.Recipe
import com.example.data.model.WorkoutRoutine
import com.example.data.remote.GeminiService
import com.example.data.repository.NutriLensRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

sealed class Screen {
    object Onboarding : Screen()
    object Dashboard : Screen()
    object DietPlan : Screen()
    object Scanner : Screen()
    object FoodDetail : Screen()
    object Recipes : Screen()
    object Workout : Screen()
    object Chatbot : Screen()
}

sealed class ScanUiState {
    object Idle : ScanUiState()
    object Scanning : ScanUiState()
    data class Success(val food: FoodScanResult) : ScanUiState()
    data class Error(val message: String) : ScanUiState()
}

data class DayItem(
    val dayOfWeek: String, // Mon, Tue, etc.
    val dayOfMonth: String, // 08, 09, 11
    val dateString: String, // yyyy-MM-dd
    val isSelected: Boolean
)

class NutriLensViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NutriLensRepository
    private val geminiService = GeminiService()

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val todayDateString: String = dateFormat.format(Date())

    private val _selectedDate = MutableStateFlow(todayDateString)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _currentScreen = MutableStateFlow<Screen>(Screen.Dashboard)
    val currentScreen: StateFlow<Screen> = _currentScreen.asStateFlow()

    private val _scanState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanState: StateFlow<ScanUiState> = _scanState.asStateFlow()

    private val _currentScannedFood = MutableStateFlow<FoodScanResult?>(null)
    val currentScannedFood: StateFlow<FoodScanResult?> = _currentScannedFood.asStateFlow()

    private val _servingMultiplier = MutableStateFlow(1.0f)
    val servingMultiplier: StateFlow<Float> = _servingMultiplier.asStateFlow()

    private val _customDietPlan = MutableStateFlow<CustomDietPlan?>(null)
    val customDietPlan: StateFlow<CustomDietPlan?> = _customDietPlan.asStateFlow()

    private val _capturedBitmap = MutableStateFlow<Bitmap?>(null)
    val capturedBitmap: StateFlow<Bitmap?> = _capturedBitmap.asStateFlow()

    // Recipes filter
    private val _selectedRecipeCategory = MutableStateFlow("All")
    val selectedRecipeCategory: StateFlow<String> = _selectedRecipeCategory.asStateFlow()

    private val _recipeSearchQuery = MutableStateFlow("")
    val recipeSearchQuery: StateFlow<String> = _recipeSearchQuery.asStateFlow()

    // Chatbot
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.AI_COACH,
                text = "Hello! I'm Vyntra AI Coach. Ask me anything about your custom bulk/cut diet, macros, training volume, or real food alternatives."
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = NutriLensRepository(db)
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            val profile = repository.userProfile.firstOrNull()
            if (profile == null || !profile.onboardingCompleted) {
                _currentScreen.value = Screen.Onboarding
            } else {
                _customDietPlan.value = DietPlanGenerator.generatePlan(
                    name = profile.name,
                    age = profile.age,
                    gender = profile.gender,
                    weightKg = profile.weightKg,
                    heightCm = profile.heightCm,
                    activityLevel = profile.activityLevel,
                    goal = profile.goal,
                    dietaryPreference = profile.dietaryRestriction,
                    customCalorieTarget = profile.dailyCalorieTarget
                )
            }
        }
    }

    val userProfile: StateFlow<UserProfileEntity?> = repository.userProfile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentMeals: StateFlow<List<MealEntity>> = _selectedDate
        .flatMapLatest { date -> repository.getMealsByDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val currentWater: StateFlow<WaterLogEntity?> = _selectedDate
        .flatMapLatest { date -> repository.getWaterLog(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val currentWorkouts: StateFlow<List<WorkoutLogEntity>> = _selectedDate
        .flatMapLatest { date -> repository.getWorkoutsByDate(date) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Week selector days
    val weekDays: StateFlow<List<DayItem>> = _selectedDate.combine(MutableStateFlow(Unit)) { selected, _ ->
        val calendar = Calendar.getInstance()
        val days = mutableListOf<DayItem>()
        // Show current week: Mon to Sun
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        val dayNameFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val dayNumFormat = SimpleDateFormat("dd", Locale.getDefault())

        for (i in 0..6) {
            val date = calendar.time
            val dStr = dateFormat.format(date)
            days.add(
                DayItem(
                    dayOfWeek = dayNameFormat.format(date),
                    dayOfMonth = dayNumFormat.format(date),
                    dateString = dStr,
                    isSelected = dStr == selected
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        days
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDate(dateString: String) {
        _selectedDate.value = dateString
    }

    fun navigateTo(screen: Screen) {
        _currentScreen.value = screen
    }

    fun setServingMultiplier(multiplier: Float) {
        _servingMultiplier.value = multiplier.coerceIn(0.25f, 4.0f)
    }

    fun scanImage(bitmap: Bitmap) {
        viewModelScope.launch {
            _capturedBitmap.value = bitmap
            _scanState.value = ScanUiState.Scanning
            val result = geminiService.analyzeFoodImage(bitmap)
            result.fold(
                onSuccess = { food ->
                    _currentScannedFood.value = food
                    _servingMultiplier.value = 1.0f
                    _scanState.value = ScanUiState.Success(food)
                    _currentScreen.value = Screen.FoodDetail
                },
                onFailure = { err ->
                    val errorMsg = err.message ?: "Could not detect food details"
                    _scanState.value = ScanUiState.Error(errorMsg)
                }
            )
        }
    }

    fun dismissScanError() {
        _scanState.value = ScanUiState.Idle
    }

    fun setManualFood(
        name: String,
        calories: Int,
        protein: Int,
        carbs: Int,
        fat: Int,
        portionGrams: Int = 200,
        description: String = "Real food logged by user",
        dietaryTag: String = "Real Food"
    ) {
        val food = FoodScanResult(
            name = name.ifBlank { "Custom Meal" },
            calories = calories.coerceAtLeast(10),
            protein = protein.coerceAtLeast(0),
            carbs = carbs.coerceAtLeast(0),
            fat = fat.coerceAtLeast(0),
            portionGrams = portionGrams,
            description = description,
            ingredients = listOf("Real whole food ingredients"),
            micronutrients = "Custom calibrated nutrients",
            dietaryTag = dietaryTag
        )
        _currentScannedFood.value = food
        _servingMultiplier.value = 1.0f
        _scanState.value = ScanUiState.Success(food)
        _currentScreen.value = Screen.FoodDetail
    }

    fun logDietPlanMeal(meal: DietPlanMeal) {
        viewModelScope.launch {
            val portionNum = meal.portion.filter { it.isDigit() }.toIntOrNull() ?: 250
            val mealEntity = MealEntity(
                name = meal.title,
                mealType = meal.mealType,
                calories = meal.calories,
                protein = meal.protein,
                carbs = meal.carbs,
                fat = meal.fat,
                portionGrams = portionNum,
                date = _selectedDate.value,
                imageUrl = meal.imageUrl,
                description = meal.preparation,
                ingredients = meal.ingredients.joinToString(", "),
                micronutrients = "Diet Plan (${meal.dietaryTag})"
            )
            repository.insertMeal(mealEntity)
            _currentScreen.value = Screen.Dashboard
        }
    }

    fun selectPresetFood(food: FoodScanResult) {
        _capturedBitmap.value = null
        _currentScannedFood.value = food
        _servingMultiplier.value = 1.0f
        _scanState.value = ScanUiState.Success(food)
        _currentScreen.value = Screen.FoodDetail
    }

    fun logCurrentFoodAsMeal(mealType: String) {
        val food = _currentScannedFood.value ?: return
        val multiplier = _servingMultiplier.value
        viewModelScope.launch {
            val meal = MealEntity(
                name = food.name,
                mealType = mealType,
                calories = (food.calories * multiplier).toInt(),
                protein = (food.protein * multiplier).toInt(),
                carbs = (food.carbs * multiplier).toInt(),
                fat = (food.fat * multiplier).toInt(),
                portionGrams = (food.portionGrams * multiplier).toInt(),
                date = _selectedDate.value,
                imageUrl = food.imageUrl,
                description = food.description,
                ingredients = food.ingredients.joinToString(", "),
                micronutrients = food.micronutrients
            )
            repository.insertMeal(meal)
            _scanState.value = ScanUiState.Idle
            _currentScreen.value = Screen.Dashboard
        }
    }

    fun deleteMeal(id: Int) {
        viewModelScope.launch {
            repository.deleteMealById(id)
        }
    }

    fun addWaterGlass() {
        viewModelScope.launch {
            repository.updateWaterLog(_selectedDate.value, deltaGlasses = 1, glassSizeFlOz = 8)
        }
    }

    fun removeWaterGlass() {
        viewModelScope.launch {
            repository.updateWaterLog(_selectedDate.value, deltaGlasses = -1, glassSizeFlOz = 8)
        }
    }

    fun logWorkout(routine: WorkoutRoutine) {
        viewModelScope.launch {
            val workout = WorkoutLogEntity(
                date = _selectedDate.value,
                title = routine.title,
                category = routine.goalType,
                durationMinutes = routine.durationMinutes,
                caloriesBurned = routine.estimatedCalories
            )
            repository.logWorkout(workout)
            _currentScreen.value = Screen.Dashboard
        }
    }

    fun setRecipeCategory(category: String) {
        _selectedRecipeCategory.value = category
    }

    fun setRecipeSearchQuery(query: String) {
        _recipeSearchQuery.value = query
    }

    fun logRecipeDirectly(recipe: Recipe, mealType: String = "Lunch") {
        viewModelScope.launch {
            val meal = MealEntity(
                name = recipe.title,
                mealType = mealType,
                calories = recipe.calories,
                protein = recipe.protein,
                carbs = recipe.carbs,
                fat = recipe.fat,
                portionGrams = 250,
                date = _selectedDate.value,
                imageUrl = recipe.imageUrl,
                description = recipe.description,
                ingredients = recipe.ingredients.joinToString(", "),
                micronutrients = "Balanced nutrients & vitamins"
            )
            repository.insertMeal(meal)
            _currentScreen.value = Screen.Dashboard
        }
    }

    fun sendMessageToCoach(text: String) {
        if (text.isBlank()) return
        val userMsg = ChatMessage(sender = MessageSender.USER, text = text)
        _chatMessages.value = _chatMessages.value + userMsg
        _isChatLoading.value = true

        viewModelScope.launch {
            val profile = userProfile.value
            val goal = profile?.goal ?: "CUT"
            val meals = currentMeals.value
            val totalCal = meals.sumOf { it.calories }
            val targetCal = profile?.dailyCalorieTarget ?: 2400
            val remainingCal = (targetCal - totalCal).coerceAtLeast(0)
            val mealSummary = meals.joinToString("; ") { "${it.mealType}: ${it.name} (${it.calories} kcal)" }

            val history = _chatMessages.value.map {
                (if (it.sender == MessageSender.USER) "USER" else "AI") to it.text
            }

            val reply = geminiService.chatWithCoach(
                userMessage = text,
                userGoal = goal,
                caloriesRemaining = remainingCal,
                loggedMealsSummary = mealSummary,
                history = history
            )

            _chatMessages.value = _chatMessages.value + ChatMessage(
                sender = MessageSender.AI_COACH,
                text = reply
            )
            _isChatLoading.value = false
        }
    }

    fun updateProfile(
        name: String,
        age: Int,
        gender: String,
        weightKg: Float,
        heightCm: Float,
        activityLevel: String,
        goal: String,
        dietaryPreference: String,
        customCalorieTarget: Int? = null
    ) {
        val metrics = DietPlanGenerator.calculateMetrics(
            age = age,
            gender = gender,
            weightKg = weightKg,
            heightCm = heightCm,
            activityLevel = activityLevel,
            goal = goal,
            customCalorieTarget = customCalorieTarget
        )

        val plan = DietPlanGenerator.generatePlan(
            name = name,
            age = age,
            gender = gender,
            weightKg = weightKg,
            heightCm = heightCm,
            activityLevel = activityLevel,
            goal = goal,
            dietaryPreference = dietaryPreference,
            customCalorieTarget = customCalorieTarget
        )
        _customDietPlan.value = plan

        val newProfile = UserProfileEntity(
            id = 1,
            name = name.ifBlank { "Athlete" },
            age = age,
            gender = gender,
            weightKg = weightKg,
            heightCm = heightCm,
            activityLevel = activityLevel,
            goal = goal,
            dietaryRestriction = dietaryPreference,
            dailyCalorieTarget = metrics.targetCalories,
            dailyProteinTarget = metrics.proteinGrams,
            dailyCarbsTarget = metrics.carbsGrams,
            dailyFatTarget = metrics.fatGrams,
            dailyWaterTargetFlOz = metrics.waterFlOz,
            customDietPlanJson = "",
            onboardingCompleted = true
        )

        viewModelScope.launch {
            repository.saveUserProfile(newProfile)
            _currentScreen.value = Screen.Dashboard
        }
    }
}
