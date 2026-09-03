package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.data.model.PresetData
import com.example.data.model.Recipe
import com.example.ui.theme.NutriBg
import com.example.ui.theme.NutriBlack
import com.example.ui.theme.NutriBorder
import com.example.ui.theme.NutriFlameOrange
import com.example.ui.theme.NutriGray
import com.example.ui.theme.NutriGreenAccent
import com.example.ui.theme.NutriWhite
import com.example.ui.viewmodel.NutriLensViewModel

@Composable
fun RecipesScreen(
    viewModel: NutriLensViewModel,
    modifier: Modifier = Modifier
) {
    val selectedCategory by viewModel.selectedRecipeCategory.collectAsStateWithLifecycle()
    val searchQuery by viewModel.recipeSearchQuery.collectAsStateWithLifecycle()

    val categories = listOf("All", "Vegan", "Protein", "Snacks")

    val filteredRecipes = remember(selectedCategory, searchQuery) {
        PresetData.recipes.filter { recipe ->
            val matchesCategory = (selectedCategory == "All") || (recipe.category.equals(selectedCategory, ignoreCase = true))
            val matchesSearch = searchQuery.isBlank() || recipe.title.contains(searchQuery, ignoreCase = true)
            matchesCategory && matchesSearch
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(NutriBg),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 16.dp, bottom = 100.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            ) {
                Text(
                    text = "Explore Recipes",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp
                    ),
                    color = NutriBlack
                )
                Text(
                    text = "AI curated meals balanced for your macro targets",
                    style = MaterialTheme.typography.bodyMedium,
                    color = NutriGray
                )
            }
        }

        // Search Bar (matching mockup)
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setRecipeSearchQuery(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("recipe_search_input"),
                placeholder = { Text("Search foods, recipes...", color = NutriGray) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search",
                        tint = NutriGray
                    )
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = NutriBlack,
                    unfocusedTextColor = NutriBlack,
                    focusedContainerColor = NutriWhite,
                    unfocusedContainerColor = NutriWhite,
                    focusedBorderColor = NutriBlack,
                    unfocusedBorderColor = NutriBorder
                )
            )
        }

        // Category Filter Chips (matching mockup: All, Vegan, Protein, Snacks)
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) NutriBlack else NutriWhite)
                            .border(1.dp, if (isSelected) NutriBlack else NutriBorder, RoundedCornerShape(20.dp))
                            .clickable { viewModel.setRecipeCategory(cat) }
                            .padding(horizontal = 18.dp, vertical = 10.dp)
                            .testTag("recipe_filter_${cat.lowercase()}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) NutriWhite else NutriBlack
                        )
                    }
                }
            }
        }

        // Trending Recipes Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Trending Recipes (${filteredRecipes.size})",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = NutriBlack
                )
            }
        }

        // Recipes List
        items(filteredRecipes, key = { it.id }) { recipe ->
            RecipeCard(
                recipe = recipe,
                onLogClick = {
                    viewModel.logRecipeDirectly(recipe)
                }
            )
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onLogClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .background(NutriWhite)
            .border(1.dp, NutriBorder, RoundedCornerShape(22.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Food Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
            ) {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = recipe.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Category tag on top right
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NutriBlack.copy(alpha = 0.75f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = recipe.category,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = NutriWhite
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Title & Calories Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    ),
                    color = NutriBlack,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${recipe.calories} kcal",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = NutriFlameOrange
                    )
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Time & Macros
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccessTime,
                        contentDescription = "Time",
                        tint = NutriGray,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "${recipe.timeMinutes} min  •  ${recipe.difficulty}",
                        style = MaterialTheme.typography.labelSmall,
                        color = NutriGray
                    )
                }

                Text(
                    text = "P: ${recipe.protein}g  C: ${recipe.carbs}g  F: ${recipe.fat}g",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                    color = NutriBlack
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Quick log button
            Button(
                onClick = onLogClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF3F2EE),
                    contentColor = NutriBlack
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Log Meal",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Log to Diary",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}
