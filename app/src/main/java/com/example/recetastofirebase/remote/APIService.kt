package com.example.recetastofirebase.remote

import com.example.recetastofirebase.model.CategoriesList
import retrofit2.http.GET

interface APIService {
    @GET("categories.php")
    suspend fun getCategories(): CategoriesList
}
