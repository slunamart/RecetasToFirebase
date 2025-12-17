package com.example.recetastofirebase.gui

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.recetastofirebase.model.Category
import com.example.recetastofirebase.vm.CategoriesVM
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.recetastofirebase.model.Screen
import com.example.recetastofirebase.vm.RecipesVM


@Composable
fun RecipeApp(navController: NavHostController) {
    val catVM: CategoriesVM = viewModel()
    val recipesVM: RecipesVM = viewModel()
    val viewState by catVM.categoriesState
    val context = LocalContext.current

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.CategoriesGUI.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = Screen.CategoriesGUI.route) {
                CategoriesView(
                    viewState = viewState,
                    navigateToDetail = { category ->
                        navController.currentBackStackEntry
                            ?.savedStateHandle
                            ?.set("cat", category)

                        navController.navigate(Screen.CategoryDetailScreen.route)
                    }
                )
            }

            composable(route = Screen.CategoryDetailScreen.route) {
                val category = navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<Category>("cat") ?: Category("", "", "", "")

                CategoryCategoryDetailScreen(category = category)
            }

            composable(route = Screen.NewRecipe.route) {
                NewRecipeScreen(
                    uploadToFirebase = {receta ->
                        recipesVM.createRecipe(receta){ ok, errormsg ->
                            if (ok) {
                                Toast.makeText(
                                    context,
                                    "Receta guardada correctamente",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    errormsg ?: "Error al guardar la receta",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    },
                    onBack = { navController.popBackStack() }
                )
            }

            composable(Screen.MyRecipes.route) {
                MyRecipesScreen()
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    NavigationBar {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.CategoriesGUI.route) },
            label = { Text("Home") },
            icon = { Icon(Icons.Default.Home, contentDescription = null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.NewRecipe.route) },
            label = { Text("Nueva receta") },
            icon = { Icon(Icons.Default.Add, contentDescription = null) }
        )

        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(Screen.MyRecipes.route) },
            label = { Text("Mis recetas") },
            icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = null) } // o el icono que prefieras
        )
    }
}

