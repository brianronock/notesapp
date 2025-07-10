package com.example.notesapp.models

import com.google.firebase.Timestamp

/**
 * Represents a user in the NotesApp.
 * This model is stored in Firestore under the 'users' collection.
 */
data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val registeredAt: Timestamp = Timestamp.now()
)
