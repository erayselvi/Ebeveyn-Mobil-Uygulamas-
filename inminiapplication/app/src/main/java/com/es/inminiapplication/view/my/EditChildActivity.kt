package com.es.inminiapplication.view.my

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.es.inminiapplication.R

class EditChildActivity : AppCompatActivity() {

    private lateinit var editTextFirstName: EditText
    private lateinit var editTextBirthplace: EditText
    private lateinit var spinnerCity: Spinner
    private lateinit var spinnerBloodType: Spinner
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var radioButtonMale: RadioButton
    private lateinit var radioButtonFemale: RadioButton
    private lateinit var buttonUpdate: Button
    private lateinit var imageViewDelete: ImageView

    private lateinit var childDocumentId: String
    private lateinit var childRef: DocumentReference

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_child)

        // Initialize views
        editTextFirstName = findViewById(R.id.editTextFirstName)
        editTextBirthplace = findViewById(R.id.editTextBirthplace)
        spinnerCity = findViewById(R.id.spinnerCity)
        spinnerBloodType = findViewById(R.id.spinnerBloodType)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        radioButtonMale = findViewById(R.id.radioButtonMale)
        radioButtonFemale = findViewById(R.id.radioButtonFemale)
        buttonUpdate = findViewById(R.id.buttonUpdate)
        imageViewDelete = findViewById(R.id.imageViewDelete)
        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE

        // Get child document ID from Intent
        childDocumentId = intent.getStringExtra(CHILD_DOCUMENT_ID) ?: ""

        // Initialize childRef
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val db = FirebaseFirestore.getInstance()
            childRef = db.collection("Users").document(userId)
                .collection("Children").document(childDocumentId)
        }
        showProgressBar()
        // Load child information
        loadChildInformation()

        // Set onClickListener for the update button
        buttonUpdate.setOnClickListener {
            showProgressBar()
            // Update child information
            updateChildInformation()
        }
    }

    private fun loadChildInformation() {
        childRef.get()
            .addOnSuccessListener {
                    documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val childName = documentSnapshot.getString("name")
                    val birthplace = documentSnapshot.getString("date")
                    val city = documentSnapshot.getString("city")
                    val bloodType = documentSnapshot.getString("bloodType")
                    val gender = documentSnapshot.getString("gender")
                    supportActionBar?.title = "${childName} | Düzenle"
                    // Set retrieved values to UI elements
                    editTextFirstName.setText(childName)
                    editTextBirthplace.setText(birthplace)

                    // Set selected items in spinners
                    val cityIndex = (spinnerCity.adapter as ArrayAdapter<String>).getPosition(city)
                    spinnerCity.setSelection(cityIndex)

                    val bloodTypeIndex = (spinnerBloodType.adapter as ArrayAdapter<String>).getPosition(bloodType)
                    spinnerBloodType.setSelection(bloodTypeIndex)

                    // Check the appropriate radio button
                    if (gender == "Erkek") {
                        radioButtonMale.isChecked = true
                    } else if (gender == "Kadın") {
                        radioButtonFemale.isChecked = true
                    }
                } else {
                    Toast.makeText(this, "Child not found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error retrieving child information: $e", Toast.LENGTH_SHORT).show()

            }
        hideProgressBar()
    }

    private fun updateChildInformation() {
        val updatedChild = hashMapOf(
            "name" to editTextFirstName.text.toString(),
            "birthplace" to editTextBirthplace.text.toString(),
            "city" to spinnerCity.selectedItem.toString(),
            "bloodType" to spinnerBloodType.selectedItem.toString(),
            "gender" to when {
                radioButtonMale.isChecked -> "Erkek"
                radioButtonFemale.isChecked -> "Kadın"
                else -> ""
            }
        )

        Log.d("EditChildActivity", "Güncellenen Çocuk Bilgisi: $updatedChild")

        childRef.set(updatedChild, SetOptions.merge())
            .addOnSuccessListener {
                Log.d("EditChildActivity", "Çocuk bilgileri başarıyla güncellendi")
                hideProgressBar()
                finish()
                Toast.makeText(this, "Çocuk bilgileri başarıyla güncellendi", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("EditChildActivity", "Çocuk bilgilerini güncelleme hatası: $e")
                hideProgressBar()
                Toast.makeText(this, "Çocuk bilgilerini güncelleme hatası: $e", Toast.LENGTH_SHORT).show()
            }

    }
    fun onDeleteButtonClick(view : View){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Çocuğu Sil")
        builder.setMessage("Çocuğu silmek istediğinize emin misiniz?")
        builder.setPositiveButton("Evet") { _: DialogInterface, _: Int ->
            // Kullanıcı "Evet" dediğinde çocuğu sil
            showProgressBar()
            deleteChild()
        }
        builder.setNegativeButton("Hayır") { _: DialogInterface, _: Int ->
            // Kullanıcı "Hayır" dediğinde işlemi iptal et
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun deleteChild() {
        // Çocuğu silme işlemini burada gerçekleştir
        childRef.delete()
            .addOnSuccessListener {
                Toast.makeText(this, "Çocuk başarıyla silindi", Toast.LENGTH_SHORT).show()
                hideProgressBar()
                finish() // Aktiviteyi kapat
            }
            .addOnFailureListener { e ->
                hideProgressBar()
                Toast.makeText(this, "Çocuk silme hatası: $e", Toast.LENGTH_SHORT).show()
            }
    }

    companion object {
        const val CHILD_DOCUMENT_ID = "child_document_id"
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
