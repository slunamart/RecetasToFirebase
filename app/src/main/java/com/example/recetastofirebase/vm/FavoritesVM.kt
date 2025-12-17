package com.example.recetastofirebase.vm

import androidx.lifecycle.ViewModel
import com.example.recetastofirebase.model.Category
import com.example.recetastofirebase.remote.FavoritesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesVM : ViewModel() {

    private val _message = MutableStateFlow<String?>(null)
    val message = _message.asStateFlow()

    fun addFavorite(category: Category) {
        FavoritesRepository.addFavoriteCategory(category) { ok, msg ->
            _message.value =
                if (ok) "Añadida a favoritas"
                else msg ?: "Error al guardar"
        }
    }

    fun clearMessage() {
        _message.value = null
    }
}
