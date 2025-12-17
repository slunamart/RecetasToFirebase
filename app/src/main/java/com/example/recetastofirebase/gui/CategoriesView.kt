package com.example.recetastofirebase.gui

import com.example.recetastofirebase.model.Category
import com.example.recetastofirebase.vm.CategoriesVM
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier


@Composable
fun CategoriesView(
    modifier: Modifier = Modifier,
    viewState : CategoriesVM.CategoriesFetchState,
    navigateToDetail: (Category) -> Unit
){
    Box(modifier = Modifier.fillMaxSize()){
        when{
            viewState.loading ->{
                CircularProgressIndicator(modifier.align(Alignment.Center))
            }

            viewState.error != null ->{
                Text("Ocurrió un error: $viewState.error")
            }
            else ->{
                CategoryScreen(categories = viewState.categories, navigateToDetail )
            }
        }
    }
}

@Composable
fun CategoryScreen(
    categories: List<Category>,
    navigateToDetail: (Category) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2), // Definimos que tenga dos columnas
        modifier = Modifier.fillMaxSize()
    ) {
        items(categories) { category ->
            CategoryItem(category = category, navigateToDetail)
        }
    }
}

