package com.es.inminiapplication.view.app

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Length
import com.es.inminiapplication.model.Note
import com.es.inminiapplication.view.my.EditChildActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


private lateinit var auth: FirebaseAuth
private lateinit var firestore: FirebaseFirestore
private lateinit var userId: String
private lateinit var progressBar: ProgressBar

class LengthAddActivity : AppCompatActivity() {

    private lateinit var datePickerText: EditText
    private lateinit var saveButton: Button
    private lateinit var childDocumentId: String

    private lateinit var lengthvalueText: EditText




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length_add)
        childDocumentId = intent.getStringExtra(LengthActivity.CHILD_DOCUMENT_ID) ?: ""
        supportActionBar?.title = "Not Ekle"
        auth = Firebase.auth
        firestore = Firebase.firestore


        userId = auth.currentUser?.uid ?: ""

        lengthvalueText = findViewById(R.id.lengthvalueText)
        datePickerText = findViewById(R.id.datePicker)

        saveButton = findViewById(R.id.saveButton)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        saveButton.setOnClickListener {
            showProgressBar()
            val lengthvalue = lengthvalueText.text.toString()
            val datePicker = datePickerText.text.toString()

            if (lengthvalue.isNotEmpty() && datePicker.isNotEmpty()) {
                val length = Length(lengthvalue, datePicker)
                saveLength(length)
            } else {
                hideProgressBar()
                Toast.makeText(this, "Boy ve tarih bilgisi gereklidir.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun saveLength(length: Length) {
        Log.i("BOY", "${childDocumentId}")
        // Firestore işlemlerini gerçekleştirirken
        val lengthCollection = firestore.collection("Users").document(userId).collection("Children").document(childDocumentId)
        lengthCollection.collection("Lengths").add(length)
            .addOnSuccessListener {
                Toast.makeText(this, "Boy kaydedildi.", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                finish()

                val intent = Intent(this@LengthAddActivity, LengthSChildActivity::class.java)
                startActivity(intent)

            }.addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this, "Boy kaydedilirken hata oluştu.", Toast.LENGTH_SHORT).show()
            }
    }
    fun showDatePicker(view: View) {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("tr"))
                datePickerText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
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


    companion object {
        const val CHILD_DOCUMENT_ID = "child_document_id"
    }
}
