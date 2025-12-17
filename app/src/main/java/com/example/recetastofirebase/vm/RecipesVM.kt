package com.example.recetastofirebase.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.recetastofirebase.model.PersonalList
import com.example.recetastofirebase.model.RecipeDoc
import com.example.recetastofirebase.model.RecipesUiState
import com.example.recetastofirebase.remote.RecipesRepository


class RecipesVM : ViewModel() {

    var uiState by mutableStateOf(RecipesUiState(isLoading = true))
        private set

    fun loadRecipes() {
        uiState = uiState.copy(isLoading = true, error = null)

        RecipesRepository.getRecipes(
            onResult = { list ->
                uiState = RecipesUiState(
                    isLoading = false,
                    recipes = list,
                    error = null
                )
            },
            onError = { e ->
                uiState = RecipesUiState(
                    isLoading = false,
                    recipes = emptyList(),
                    error = e.message ?: "Error al cargar recetas"
                )
            }
        )
    }

    fun deleteRecipe(id: String) {
        RecipesRepository.deleteRecipe(id) { ok, msg ->
            if (ok) {
                uiState = uiState.copy(
                    recipes = uiState.recipes.filterNot { it.id == id }
                )
            } else {
                uiState = uiState.copy(error = msg ?: "Error borrando receta")
            }
        }
    }

    fun updateRecipe(id: String, newRecipe: PersonalList) {
        RecipesRepository.updateRecipe(id, newRecipe) { ok, msg ->
            if (ok) {
                uiState = uiState.copy(
                    recipes = uiState.recipes.map {
                        if (it.id == id) RecipeDoc(id, newRecipe) else it
                    }
                )
            } else {
                uiState = uiState.copy(error = msg ?: "Error actualizando receta")
            }
        }
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    fun createRecipe(
        recipe: PersonalList,
        onResult: (Boolean, String?) -> Unit
    ) {
        RecipesRepository.uploadRecipe(recipe) { ok, msg ->
            onResult(ok, msg)
            if (ok) loadRecipes() // opcional: refresca "Mis recetas"
        }
    }

}
