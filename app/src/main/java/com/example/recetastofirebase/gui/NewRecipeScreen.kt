package com.example.recetastofirebase.gui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.recetastofirebase.model.PersonalList


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewRecipeScreen(
    uploadToFirebase: (PersonalList) -> Unit,
    onBack: (() -> Unit)? = null
) {
    var nombre by remember { mutableStateOf("") }
    var ingredientesText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nueva receta") },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver"
                            )
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // Nombre
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la receta") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Ingredientes (opcional)
            OutlinedTextField(
                value = ingredientesText,
                onValueChange = { ingredientesText = it },
                label = { Text("Ingredientes (uno por línea, opcional)") },
                placeholder = { Text("Ej.:\n200g pasta\nTomate\nQueso rallado") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 6
            )

            // Descripción
            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                maxLines = 6
            )

            // Precio
            OutlinedTextField(
                value = precioText,
                onValueChange = { nuevo ->
                    // Sólo permitimos números y un punto/coma decimal
                    if (nuevo.isEmpty() || nuevo.matches(Regex("""\d*([.,]\d*)?"""))) {
                        precioText = nuevo.replace(',', '.') // normalizamos a punto
                    }
                },
                label = { Text("Precio (€)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(
                    text = errorMessage!!,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    // Validación
                    if (nombre.isBlank() || descripcion.isBlank() || precioText.isBlank()) {
                        errorMessage =
                            "Nombre, descripción y precio son obligatorios."
                        return@Button
                    }

                    val precio = precioText.toDoubleOrNull()
                    if (precio == null) {
                        errorMessage = "Introduce un precio numérico válido."
                        return@Button
                    }

                    val ingredientesList = ingredientesText
                        .lines()
                        .map { it.trim() }
                        .filter { it.isNotEmpty() }

                    val nuevaReceta = PersonalList(
                        nombre = nombre.trim(),
                        ingredientes = ingredientesList,
                        description = descripcion.trim(),
                        precio = precio
                    )

                    errorMessage = null
                    uploadToFirebase(nuevaReceta)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Guardar receta")
            }
        }
    }
}

