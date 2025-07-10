package com.example.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.example.notesapp.models.Note

/**
 * ViewModel for managing notes in-memory.
 * Provides functionality to add, update, and delete notes.
 */
class NotesViewModel : ViewModel() {

    private val db = Firebase.firestore
    val notes = mutableStateListOf<Note>()

    init {
        loadNotes()
    }

    fun addNote(note: Note) {
        notes.add(note) // Add to local state
        db.collection("notes")
            .add(note)
            .addOnSuccessListener { Log.d("Firestore", "Note added!") }
            .addOnFailureListener { e -> Log.e("Firestore", "Error adding note", e) }
    }

    fun updateNote(index: Int, updatedNote: Note) {
        if (index in notes.indices) {
            val oldNote = notes[index]
            notes[index] = updatedNote

            // Update Firestore document
            db.collection("notes")
                .whereEqualTo("title", oldNote.title)
                .whereEqualTo("content", oldNote.content)
                .limit(1)
                .get()
                .addOnSuccessListener { snapshot ->
                    if (!snapshot.isEmpty) {
                        val docId = snapshot.documents[0].id
                        db.collection("notes").document(docId).set(updatedNote)
                    }
                }
                .addOnFailureListener { e -> Log.e("Firestore", "Error updating note", e) }
        }
    }

    fun loadNotes() {
        db.collection("notes")
            .orderBy("date", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                notes.clear()
                for (doc in result) {
                    try {
                        val note = doc.toObject(Note::class.java)
                        notes.add(note)
                    } catch (e: Exception) {
                        Log.e("Firestore", "Failed to parse note: ${doc.id}", e)
                    }
                }
                Log.d("Firestore", "Notes loaded: ${notes.size}")
            }
            .addOnFailureListener { e ->
                Log.e("Firestore", "Error loading notes", e)
            }
    }
}