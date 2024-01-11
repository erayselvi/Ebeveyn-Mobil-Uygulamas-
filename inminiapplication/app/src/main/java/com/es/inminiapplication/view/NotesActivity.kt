package com.es.inminiapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.NotesAdapter
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NotesActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesAdapter: NotesAdapter
    private lateinit var addNoteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)

        auth = Firebase.auth
        firestore = Firebase.firestore

        userId = auth.currentUser?.uid ?: ""

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        addNoteButton = findViewById(R.id.addNoteButton)

        setupRecyclerView()
        loadNotes()

        addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter()
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesAdapter
    }

    private fun loadNotes() {
        firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e(TAG, "Hata oluştu: $error")
                    return@addSnapshotListener
                }

                val notes = ArrayList<Note>()
                for (document in value!!) {
                    val note = document.toObject(Note::class.java)
                    notes.add(note)
                }

                notesAdapter.setNotes(notes)
            }
    }

    companion object {
        private const val TAG = "NotesActivity"
    }
}
