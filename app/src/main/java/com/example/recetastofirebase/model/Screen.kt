package com.example.recetastofirebase.model

sealed class Screen(val route: String) {
    data object CategoriesGUI : Screen("recipe_screen")
    data object CategoryDetailScreen : Screen("detail_screen")
    data object NewRecipe : Screen("new_recipe")
    data object MyRecipes : Screen("myRecipes")

}