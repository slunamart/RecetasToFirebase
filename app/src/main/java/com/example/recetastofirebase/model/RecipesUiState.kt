package com.example.recetastofirebase.model

data class RecipesUiState(
    val isLoading: Boolean = false,
    val recipes: List<RecipeDoc> = emptyList(),
    val error: String? = null
)