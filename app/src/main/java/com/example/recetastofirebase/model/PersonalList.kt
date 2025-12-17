package com.example.recetastofirebase.model

data class PersonalList(
    val nombre: String = "",
    val ingredientes: List<String> = emptyList(),
    val description: String = "",
    val precio: Double = 0.0
)
