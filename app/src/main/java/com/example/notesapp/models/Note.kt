package com.example.notesapp.models

import com.google.firebase.firestore.DocumentId

/**
 * Data class representing a note item.
 */
data class Note(
    @DocumentId var id: String = "",
    val title: String = "",
    val content: String = "",
    val tag: String = "",
    val date: String = "",
    val isBold: Boolean = false,
    val isItalic: Boolean = false,
    val isUnderline: Boolean = false
)