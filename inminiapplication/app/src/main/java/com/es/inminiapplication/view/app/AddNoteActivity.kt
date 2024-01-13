package com.es.inminiapplication.view.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Note
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AddNoteActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var userId: String

    private lateinit var titleEditText: EditText
    private lateinit var contentEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        supportActionBar?.title = "Not Ekle"
        auth = Firebase.auth
        firestore = Firebase.firestore

        userId = auth.currentUser?.uid ?: ""

        titleEditText = findViewById(R.id.titleEditText)
        contentEditText = findViewById(R.id.contentEditText)
        saveButton = findViewById(R.id.saveButton)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        saveButton.setOnClickListener {
            showProgressBar()
            val title = titleEditText.text.toString()
            val content = contentEditText.text.toString()

            if (title.isNotEmpty() && content.isNotEmpty()) {
                val note = Note(title, content, userId)
                saveNote(note)
            } else {
                hideProgressBar()
                Toast.makeText(this, "Başlık ve içerik gereklidir.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveNote(note: Note) {
        firestore.collection("notes")
            .add(note)
            .addOnSuccessListener {
                Toast.makeText(this, "Not kaydedildi.", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                finish()
            }
            .addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this, "Not kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
        // Kullanıcının diğer işlemleri başlatmasını engellemek için gerekirse arka planı kapatabilirsiniz.
        window.setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
