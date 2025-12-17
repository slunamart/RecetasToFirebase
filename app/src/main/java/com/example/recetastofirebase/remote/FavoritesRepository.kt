package com.example.recetastofirebase.remote

import android.annotation.SuppressLint

import com.example.recetastofirebase.model.Category
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

object FavoritesRepository {
    @SuppressLint("StaticFieldLeak")
    private val db = Firebase.firestore
    private val favsCollection = db.collection("colecciones_favoritas")

    fun addFavoriteCategory(
        category: Category,
        onResult: (Boolean, String?) -> Unit
    ) {
        val data = mapOf(
            "name" to category.strCategory,
            "thumb" to category.strCategoryThumb,
            "description" to category.strCategoryDescription,
            "createdAt" to FieldValue.serverTimestamp()
        )

        // opcional: usar ID = nombre para evitar duplicados
        favsCollection.document()
            .set(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}
