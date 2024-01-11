package com.es.inminiapplication.view

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.es.inminiapplication.databinding.ActivityAddchildBinding

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddChildActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddchildBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddchildBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore
    }

    fun addchildclicker(view : View){

        val currentUser = auth.currentUser

        val name = binding.nameText.text.toString()
        val date = binding.dateText.text.toString()

        val selectedGenderRadioButtonId = binding.genderRadioGroup.checkedRadioButtonId

        if (name.isEmpty() || date.isEmpty() || selectedGenderRadioButtonId == -1) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val genderRadioButton: RadioButton = findViewById(selectedGenderRadioButtonId)
        val gender = genderRadioButton.text.toString()

        val userMap = hashMapOf<String, Any>()
        userMap["name"] = name
        userMap["date"] = date
        userMap["gender"] = gender



        if (currentUser != null) {
            firestore.collection("Users")
                .document(currentUser.uid)
                .collection("Children")
                .add(userMap)
                .addOnCompleteListener { childCreationTask ->
                    if (childCreationTask.isSuccessful) {
                        // Çocuk oluşturulduktan sonra aşı takvimi ekleniyor
                        val childDocumentId = childCreationTask.result?.id
                        if (childDocumentId != null) {
                            // Aşı takvimi başlangıç verisi


                        }
                    } else {
                        Toast.makeText(
                            this@AddChildActivity,
                            "Çocuk oluşturulurken bir hata oluştu",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }

    }
    fun showDatePicker(view: View) {
        val calendar = null
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.dateText.setText(dateFormat.format(selectedDate.time))
            },
            calendar?.get(Calendar.YEAR) ?: 0,
            calendar?.get(Calendar.MONTH) ?: 0,
            calendar?.get(Calendar.DAY_OF_MONTH) ?: 0
        )
        datePickerDialog.show()
    }
}