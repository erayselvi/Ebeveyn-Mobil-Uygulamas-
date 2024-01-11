package com.es.inminiapplication.view

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.es.inminiapplication.databinding.ActivityPersonalBinding
import com.es.inminiapplication.view.app.AddChildActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PersonalActivity : AppCompatActivity() {


    private lateinit var binding:ActivityPersonalBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth
        firestore = Firebase.firestore


        val auth = FirebaseAuth.getInstance()

        if (auth.currentUser == null) {
            finish()
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
                binding.dateText.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
    fun completeClicker(view : View){


        /*val userMap = hashMapOf<String, Any>()
        userMap.put("userEmail",auth.currentUser!!.email!!)
        userMap.put("name",binding.nameText.text.toString())
        userMap.put("surname",binding.surnameText.text.toString())
        firestore.collection("Users").add(userMap).addOnSuccessListener {

            val intent = Intent(this@PersonalActivity,ChildActivity::class.java)
            startActivity(intent)
            finish()

        }.addOnFailureListener {
            Toast.makeText(this@PersonalActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
        }*/

        val currentUser = auth.currentUser

        val name = binding.nameText.text.toString()
        val surname = binding.surnameText.text.toString()
        val date = binding.dateText.text.toString()

        val selectedGenderRadioButtonId = binding.genderRadioGroup.checkedRadioButtonId

        if (name.isEmpty() || surname.isEmpty() || date.isEmpty() || selectedGenderRadioButtonId == -1) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }

        val genderRadioButton: RadioButton = findViewById(selectedGenderRadioButtonId)
        val gender = genderRadioButton.text.toString()

        val userMap = hashMapOf<String, Any>()
        userMap["userEmail"] = auth.currentUser!!.email!!
        userMap["name"] = name
        userMap["date"] = date
        userMap["gender"] = gender

        if (currentUser != null) {
            firestore.collection("Users")
                .document(currentUser.uid)
                .set(userMap)
                .addOnCompleteListener {
                    val intent = Intent(this@PersonalActivity, AddChildActivity::class.java)
                    startActivity(intent)
                    finish()
                }.addOnFailureListener {
                    Toast.makeText(this@PersonalActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        }
    }
}