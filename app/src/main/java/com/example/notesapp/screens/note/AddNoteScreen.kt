package com.example.notesapp.screens.note

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.notesapp.components.BottomBar
import com.example.notesapp.components.FormattingButton
import com.example.notesapp.models.Note
import com.example.notesapp.viewmodel.NotesViewModel

/**
 * A composable screen that allows the user to create a new note.
 * The screen contains inputs for the note title, content, and an optional tag.
 * A floating action button is used to save the note and navigate back to the home screen.
 *
 * @param navController Used to handle navigation between screens.
 */
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNoteScreen(
    navController: NavController, notesViewModel: NotesViewModel = viewModel()
) {
    // State variables for the title, content, and tag input fields
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var tag by remember { mutableStateOf("") }

    // Formatting state variables
    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }
    var isUnderline by remember { mutableStateOf(false) }

    // Scaffold provides a layout structure with a top app bar and FAB
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Add New Note") }) // App bar title
        }, floatingActionButton = {
            FloatingActionButton(onClick = {
                val date = java.time.LocalDate.now().toString()
                val newNote = Note(
                    title = title,
                    content = content,
                    tag = tag,
                    date = date,
                    isBold = isBold, // Apply formatting to the note using the state variables
                    isItalic = isItalic,
                    isUnderline = isUnderline
                )
                notesViewModel.addNote(newNote)
                navController.popBackStack()
            }) {
                Text("\u2713")
            }
        },
        // Sets the insets for the content window to be the soft keyboard's insets
        contentWindowInsets = WindowInsets.ime,
        // Bottom bar is displayed at the bottom of the screen
        bottomBar = { BottomBar(navController) }) { padding ->
        // Column for arranging input fields vertically with spacing
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Text field for the note title
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Title") },
                modifier = Modifier.fillMaxWidth()
            )

            // Formatting toolbar
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FormattingButton("B", isBold) { isBold = !isBold }
                FormattingButton("I", isItalic) { isItalic = !isItalic }
                FormattingButton("U", isUnderline) { isUnderline = !isUnderline }
            }

            // Text field for the note content (occupies remaining space)
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Write your note...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = TextStyle(
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None
                )
            )

            // Text field for the optional tag/category
            OutlinedTextField(
                value = tag,
                onValueChange = { tag = it },
                label = { Text("Tag (optional)") },
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}