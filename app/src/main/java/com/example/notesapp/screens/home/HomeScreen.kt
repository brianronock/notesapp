package com.example.notesapp.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.notesapp.components.BottomBar
import com.example.notesapp.viewmodel.NotesViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(
    navController: NavController,
    notesViewModel: NotesViewModel,
) {
    val notes = notesViewModel.notes
    val user = FirebaseAuth.getInstance().currentUser
    val name = user?.displayName ?: "User"

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate("add_note")
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        },
        bottomBar = { BottomBar(navController) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Welcome back, $name!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn {
                items(notes) { note ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable {
                                navController.navigate("view_note/${notes.indexOf(note)}")
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = note.title, style = MaterialTheme.typography.titleMedium)
                            Text(
                                text = note.content,
                                style = TextStyle(
                                    fontWeight = if (note.isBold) FontWeight.Bold else FontWeight.Normal,
                                    fontStyle = if (note.isItalic) FontStyle.Italic else FontStyle.Normal,
                                    textDecoration = if (note.isUnderline) TextDecoration.Underline else TextDecoration.None
                                )
                            )
                            Text(text = note.date, style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}