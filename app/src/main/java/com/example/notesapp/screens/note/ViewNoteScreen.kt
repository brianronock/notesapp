package com.example.notesapp.screens.note

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
import com.example.notesapp.components.FormattingButton
import com.example.notesapp.models.Note
import com.example.notesapp.viewmodel.NotesViewModel

/**
 * A screen to view and optionally edit an existing note.
 *
 * @param navController Navigation controller for moving between screens.
 * @param notesViewModel Shared view model containing all notes.
 * @param noteIndex Index of the note to view/edit.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewNoteScreen(
    navController: NavController,
    notesViewModel: NotesViewModel,
    noteIndex: Int
) {
    // Retrieve the note by index
    val note = notesViewModel.notes.getOrNull(noteIndex)
    if (note == null) {
        Text("Note not found.")
        return
    }

    // Local editable state
    var title by remember { mutableStateOf(note.title) }
    var content by remember { mutableStateOf(note.content) }
    var tag by remember { mutableStateOf(note.tag) }

    // Formatting state
    var isBold by remember { mutableStateOf(note.isBold) }
    var isItalic by remember { mutableStateOf(note.isItalic) }
    var isUnderline by remember { mutableStateOf(note.isUnderline) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Edit Note") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                notesViewModel.updateNote(
                    noteIndex,
                    Note(
                        title = title,
                        content = content,
                        tag = tag,
                        date = note.date, // keep original date
                        isBold = isBold,
                        isItalic = isItalic,
                        isUnderline = isUnderline
                    )
                )
                navController.popBackStack()
            }) {
                Text("\u2713")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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

            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Edit note...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                textStyle = TextStyle(
                    fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                    fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal,
                    textDecoration = if (isUnderline) TextDecoration.Underline else TextDecoration.None
                )
            )

            OutlinedTextField(
                value = tag,
                onValueChange = { tag = it },
                label = { Text("Tag (optional)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}