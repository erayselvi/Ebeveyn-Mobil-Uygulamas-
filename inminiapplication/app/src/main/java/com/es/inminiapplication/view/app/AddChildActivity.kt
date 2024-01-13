package com.es.inminiapplication.view.app

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.es.inminiapplication.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddChildActivity : AppCompatActivity() {

    private lateinit var TextFirstName: EditText
    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerBloodType: Spinner
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var buttonSave: Button
    private lateinit var datePicker: EditText
    private lateinit var progressBar: ProgressBar


    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addchild)

        // Initialize views
        TextFirstName = findViewById(R.id.TextFirstName)
        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerBloodType = findViewById(R.id.spinnerBloodType)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        buttonSave = findViewById(R.id.buttonUpdate)
        datePicker = findViewById(R.id.datePicker)

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE  // Başlangıçta görünmez yapın

        supportActionBar?.title="Çocuk Ekle"

        // Initialize Firestore
        db = FirebaseFirestore.getInstance()

        datePicker.setOnClickListener {
            showDatePickerDialog()
        }
        // Set onClickListener for the save button
        buttonSave.setOnClickListener {

            showProgressBar()

            if (!TextFirstName.text.isNullOrEmpty() && !datePicker.text.isNullOrEmpty())
            {
                saveChildInformation()
            }else {
                Toast.makeText(this, "Lütfen bilgileri doldurunuz.", Toast.LENGTH_SHORT).show()
                hideProgressBar()

            }

        }
    }

    private fun saveChildInformation() {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val child = hashMapOf(
                "name" to TextFirstName.text.toString(),
                "date" to datePicker.text.toString(),
                "city" to spinnerCity.selectedItem.toString(),
                "bloodType" to spinnerBloodType.selectedItem.toString(),
                "gender" to when {
                    radioButtonMale.isChecked -> "Erkek"
                    radioButtonFemale.isChecked -> "Kadın"
                    else -> ""
                }
            )

            db.collection("Users").document(userId)
                .collection("Children")
                .add(child)
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(
                        this,
                        "Çocuk bilgileri başarıyla kaydedildi",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgressBar()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        this,
                        "Çocuk bilgileri kaydetme hatası: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                    hideProgressBar()
                }
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


    companion object {
        const val CHILD_DOCUMENT_ID = "child_document_id"
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)

                // Format the selected date and set it to the datePicker EditText
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale("tr"))
                datePicker.setText(dateFormat.format(calendar.time))
            }

        val datePickerDialog = DatePickerDialog(
            this,
            dateListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )

        datePickerDialog.show()
    }
}