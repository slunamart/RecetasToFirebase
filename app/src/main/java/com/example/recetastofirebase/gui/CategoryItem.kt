package com.example.recetastofirebase.gui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.recetastofirebase.model.Category
import com.example.recetastofirebase.remote.FavoritesRepository
import com.example.recetastofirebase.vm.FavoritesVM

@Composable
fun CategoryItem(
    category: Category,
    navigateToDetail: (Category) -> Unit
) {
    val favoritesVM: FavoritesVM = viewModel()
    val message by favoritesVM.message.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            favoritesVM.clearMessage()
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { navigateToDetail(category) }
            .padding(8.dp)
    ) {

        // ⭐ Botón favoritos arriba a la derecha
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { favoritesVM.addFavorite(category) }) {
                Icon(Icons.Default.Star, contentDescription = "Favorita")
            }
        }

        Image(
            painter = rememberAsyncImagePainter(category.strCategoryThumb),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        )

        Text(
            text = category.strCategory,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
