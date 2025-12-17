package com.example.recetastofirebase.gui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.recetastofirebase.model.PersonalList
import com.example.recetastofirebase.model.RecipeDoc
import com.example.recetastofirebase.vm.RecipesVM

@Composable
fun MyRecipesScreen(
    recipesVM: RecipesVM = viewModel()
) {
    val state = recipesVM.uiState
    var editing by remember { mutableStateOf<RecipeDoc?>(null) }

    LaunchedEffect(Unit) {
        recipesVM.loadRecipes()
    }

    when {
        state.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        state.error != null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Error: ${state.error}")
        }

        state.recipes.isEmpty() -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Todavía no has creado ninguna receta")
        }

        else -> LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(state.recipes) { doc ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                doc.data.nombre,
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.weight(1f)
                            )

                            IconButton(onClick = { editing = doc }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }

                            IconButton(onClick = { recipesVM.deleteRecipe(doc.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Borrar")
                            }
                        }

                        Spacer(Modifier.height(6.dp))
                        Text(doc.data.description)
                        Text("Ingredientes: ${doc.data.ingredientes.joinToString()}")
                        Text("Precio: ${doc.data.precio} €")
                    }
                }
            }
        }
    }

    // Dialog de edición
    editing?.let { ed ->
        var nombre by remember { mutableStateOf(ed.data.nombre) }
        var descripcion by remember { mutableStateOf(ed.data.description) }
        var ingredientesText by remember { mutableStateOf(ed.data.ingredientes.joinToString("\n")) }
        var precioText by remember { mutableStateOf(ed.data.precio.toString()) }

        AlertDialog(
            onDismissRequest = { editing = null },
            title = { Text("Editar receta") },
            text = {
                Column {
                    OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") })
                    OutlinedTextField(descripcion, { descripcion = it }, label = { Text("Descripción") })
                    OutlinedTextField(ingredientesText, { ingredientesText = it }, label = { Text("Ingredientes (1 por línea)") })
                    OutlinedTextField(precioText, { precioText = it }, label = { Text("Precio") })
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    val precio = precioText.replace(",", ".").toDoubleOrNull()
                    if (precio == null) {
                        recipesVM.clearError()
                        // puedes meter un error local si quieres
                        return@TextButton
                    }

                    val newRecipe = PersonalList(
                        nombre = nombre.trim(),
                        description = descripcion.trim(),
                        ingredientes = ingredientesText.lines().map { it.trim() }.filter { it.isNotEmpty() },
                        precio = precio
                    )

                    recipesVM.updateRecipe(ed.id, newRecipe)
                    editing = null
                }) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { editing = null }) { Text("Cancelar") }
            }
        )
    }
}