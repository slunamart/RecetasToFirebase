package com.example.recetastofirebase.remote

import android.annotation.SuppressLint

import com.example.recetastofirebase.model.Category
import com.example.recetastofirebase.model.FavoriteCategoryDoc
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

    fun getFavorites(
        onResult: (List<FavoriteCategoryDoc>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        favsCollection.get()
            .addOnSuccessListener { snapshot ->
                try {
                    val list = snapshot.documents.map { doc ->
                        FavoriteCategoryDoc(
                            id = doc.id,
                            name = doc.getString("name") ?: "",
                            thumb = doc.getString("thumb") ?: "",
                            description = doc.getString("description") ?: ""
                        )
                    }
                    onResult(list)
                } catch (e: Exception) {
                    onError(e)
                }
            }
            .addOnFailureListener(onError)
    }

    fun deleteFavorite(id: String, onResult: (Boolean, String?) -> Unit) {
        favsCollection.document(id).delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun updateFavorite(
        oldId: String,
        newName: String,
        newThumb: String,
        newDescription: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        val data = mapOf(
            "name" to newName,
            "thumb" to newThumb,
            "description" to newDescription
        )

        // si NO cambia el nombre (id), solo update
        if (oldId == newName) {
            favsCollection.document(oldId).update(data)
                .addOnSuccessListener { onResult(true, null) }
                .addOnFailureListener { e -> onResult(false, e.message) }
            return
        }

        // si cambia el nombre: crear doc nuevo + borrar el viejo
        favsCollection.document(newName).set(data)
            .addOnSuccessListener {
                favsCollection.document(oldId).delete()
                    .addOnSuccessListener { onResult(true, null) }
                    .addOnFailureListener { e -> onResult(false, e.message) }
            }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}
