package com.es.inminiapplication.view.app
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.NotesAdapter
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Note
import com.google.firebase.Timestamp
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
    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes)
        supportActionBar?.title = "Notlarım"
        auth = Firebase.auth
        firestore = Firebase.firestore

        userId = auth.currentUser?.uid ?: ""

        notesRecyclerView = findViewById(R.id.notesRecyclerView)
        addNoteButton = findViewById(R.id.addNoteButton)
        searchView = findViewById(R.id.searchView)

        setupRecyclerView()
        loadNotes()

        addNoteButton.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            startActivity(intent)
        }

        setupSearchView()
    }

    private fun setupRecyclerView() {
        notesAdapter = NotesAdapter()
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = notesAdapter
    }

    private fun loadNotes() {
        Log.e("NotesActivity", "loadNotes fonksiyona girdi")
        firestore.collection("notes")
            .whereEqualTo("userId", userId)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.e("NotesActivity", "Firestore'dan notları alma hatası", error)
                    return@addSnapshotListener
                }

                val notes = ArrayList<Note>()
                try {
                    for (document in value!!) {
                        Log.i("NotesActivity", "${document.data}")


                        val title = document.getString("title") ?: ""
                        val content = document.getString("content") ?: ""
                        val userId = document.getString("userId") ?: ""
                        val timestamp = document.getTimestamp("timestamp") ?: Timestamp.now()

                        val note = Note(title, content, userId, timestamp)
                        notes.add(note)
                    }
                } catch (e: Exception) {
                    Log.e("NotesActivity", "loadNotes Hatası: ${e.message}")
                    e.printStackTrace()
                }

                notesAdapter.setNotes(notes)  // RecyclerView'ı güncelle
                Log.i("Notes", "loadNotes Hatası: ${notes}")
            }
    }


    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                notesAdapter.filter(newText.orEmpty())
                return true
            }
        })
        // Çarpıya basıldığında eski haline dönmek için
        searchView.setOnCloseListener {
            // Çarpıya basıldığında eski haline dönmek için filtrelemeyi temizle
            loadNotes()
            false
        }
    }

    companion object {
        private const val TAG = "NotesActivity"
    }
}
