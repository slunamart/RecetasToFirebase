package com.example.recetastofirebase.remote

import com.example.recetastofirebase.model.PersonalList
import com.example.recetastofirebase.model.RecipeDoc
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore

object RecipesRepository {
    @Suppress("StaticFieldLeak") //esto quita el warning
    private var db = Firebase.firestore
    private val recipesCollection = db.collection("recipes")

    fun uploadRecipe(
        recipe: PersonalList,
        onResult: (Boolean, String?) -> Unit
    ) {
        val data = mapOf(
            "nombre" to recipe.nombre,
            "ingredientes" to recipe.ingredientes,
            "description" to recipe.description,
            "precio" to recipe.precio,
            "createdAt" to FieldValue.serverTimestamp()
        )

        recipesCollection
            .add(data)
            .addOnSuccessListener {
                onResult(true, null)
            }
            .addOnFailureListener { e ->
                onResult(false, e.message)
            }
    }

    fun getRecipes(
        onResult: (List<RecipeDoc>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        recipesCollection
            .get()
            .addOnSuccessListener { snapshot ->
                try {
                    val list = snapshot.documents.mapNotNull { doc ->
                        val recipe = doc.toObject(PersonalList::class.java) ?: return@mapNotNull null
                        RecipeDoc(doc.id, recipe)
                    }
                    onResult(list)
                } catch (e: Exception) {
                    onError(e)
                }
            }
            .addOnFailureListener(onError)
    }

    fun deleteRecipe(
        id: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        recipesCollection.document(id)
            .delete()
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }

    fun updateRecipe(
        id: String,
        recipe: PersonalList,
        onResult: (Boolean, String?) -> Unit
    ) {
        val data = mapOf(
            "nombre" to recipe.nombre,
            "ingredientes" to recipe.ingredientes,
            "description" to recipe.description,
            "precio" to recipe.precio
        )

        recipesCollection.document(id)
            .update(data)
            .addOnSuccessListener { onResult(true, null) }
            .addOnFailureListener { e -> onResult(false, e.message) }
    }
}
