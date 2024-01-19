package com.es.inminiapplication.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Note
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Locale

class NotesAdapter : RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    private var notes: List<Note> = listOf()
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        firestore = Firebase.firestore

        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        try {
            val currentNote = notes[position]
            holder.bind(currentNote)
        } catch (e: Exception) {
            Log.e("NotesAdapter", "onBindViewHolder Hatası: ${e.message}")
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    fun setNotes(notes: List<Note>) {
        Log.i("setNotes", "loadNotes : ${notes}")
        this.notes = notes
        notifyDataSetChanged()
    }

    fun filter(query: String) {
        val filteredList = if (query.isNotEmpty()) {
            // Eğer arama sorgusu boş değilse, filtreleme işlemini yap
            notes.filter {
                it.title.contains(query, ignoreCase = true) || it.content.contains(query, ignoreCase = true)
            }
        } else {
            // Eğer arama sorgusu boşsa, orijinal notları kullan
            notes
        }

        // Filtrelenmiş listeyi set et
        setNotes(filteredList)
    }

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titleTextView: TextView = itemView.findViewById(R.id.txtNoteTitle)
        private val contentTextView: TextView = itemView.findViewById(R.id.txtNoteContent)
        private val dateTextView: TextView = itemView.findViewById(R.id.txtNoteDate)
        private val deleteButton: ImageButton = itemView.findViewById(R.id.btnDelete)

        init {
            deleteButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val note = notes[position]
                    showDeleteConfirmationDialog(note)
                }
            }
        }

        private fun showDeleteConfirmationDialog(note: Note) {
            val alertDialogBuilder = AlertDialog.Builder(itemView.context)
            alertDialogBuilder.setTitle("Notu Sil")
            alertDialogBuilder.setMessage("Bu notu silmek istediğinizden emin misiniz?")
            alertDialogBuilder.setPositiveButton("Evet") { _, _ ->
                deleteNoteFromFirestore(note)
            }
            alertDialogBuilder.setNegativeButton("Hayır") { dialog, _ ->
                dialog.dismiss()
            }
            alertDialogBuilder.create().show()
        }

        private fun deleteNoteFromFirestore(note: Note) {
            firestore.collection("notes")
                .document()
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(itemView.context, "Not silindi", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { e ->
                    Log.e("NotesAdapter", "Not silme hatası: ${e.message}", e)
                    Toast.makeText(itemView.context, "Not silinirken bir hata oluştu", Toast.LENGTH_SHORT).show()
                }
        }

        fun bind(note: Note) {
            try {
                titleTextView.text = note.title
                contentTextView.text = note.content

                // Timestamp'ı tarih ve saat formatına çevir
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val formattedDate = sdf.format(note.timestamp.toDate())

                dateTextView.text = formattedDate

                Log.i("NoteViewHolder", "xxxxxxxx: ${titleTextView.text}")

            } catch (e: Exception) {
                Log.e("NoteViewHolder", "bind Hatası: ${e.message}")
                e.printStackTrace()
            }
        }
    }

}
