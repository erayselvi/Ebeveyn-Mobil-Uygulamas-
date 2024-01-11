package com.es.inminiapplication.view.my

import UserProfile
import android.app.AlertDialog
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.es.inminiapplication.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class EditProfileActivity : AppCompatActivity() {


    private lateinit var radioGroupGender: RadioGroup
    private lateinit var updateButton: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var currentUser: FirebaseUser

    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var datePicker: EditText
    private lateinit var genderRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var buttonUpdate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.title = "Profil Bilgilerimi Güncelle"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUser = firebaseAuth.currentUser!!

        firstNameEditText = findViewById(R.id.editTextFirstName)
        lastNameEditText = findViewById(R.id.editTextLastName)
        datePicker = findViewById(R.id.datePicker)
        genderRadioGroup = findViewById(R.id.radioGroupGender)
        maleRadioButton = findViewById(R.id.radioButtonMale)
        femaleRadioButton = findViewById(R.id.radioButtonFemale)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        // Tarih seçildiğinde DatePickerDialog'u aç
        datePicker.setOnClickListener {
            showDatePickerDialog()
        }

        // Kullanıcının mevcut profil bilgilerini yükle
        loadUserProfile()

        // Güncelleme butonuna tıklama işlemi
        buttonUpdate.setOnClickListener {
            // Kullanıcının girdiği yeni bilgileri al
            val newFirstName = firstNameEditText.text.toString()
            val newLastName = lastNameEditText.text.toString()
            val newBirthDate = datePicker.text.toString()
            val newGender = if (maleRadioButton.isChecked) "Erkek" else "Kadın"

            // Yeni bilgilerle bir UserProfile objesi oluştur
            val updatedUserProfile = UserProfile(newFirstName, newLastName, newBirthDate, newGender)

            // Firestore'daki Users koleksiyonundaki belgeyi güncelle
            updateUserProfile(updatedUserProfile)
        }
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        // Türkçe lokal kullanarak DatePickerDialog oluştur
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, day ->
                // Tarih seçildiğinde yapılacak işlemleri buraya ekleyebilirsiniz
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                updateDateInView(selectedDate)
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // DatePickerDialog'u açmadan önce, EditText içindeki tarih bilgisini al
        val initialDate = datePicker.text.toString().split("/")
        val initialYear = initialDate[2].toInt()
        val initialMonth = initialDate[1].toInt() - 1  // Calendar.MONTH 0-11 aralığında olduğu için 1 çıkartıyoruz
        val initialDay = initialDate[0].toInt()

        // DatePickerDialog'u açıldığında, EditText içindeki tarihi seçili olarak göster
        datePickerDialog.updateDate(initialYear, initialMonth, initialDay)

        datePickerDialog.show()
    }

    private fun updateDateInView(selectedDate: Calendar) {
        val myFormat = "dd/MM/yyyy" // İstediğiniz formata göre burayı değiştirin
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())

        datePicker.setText(sdf.format(selectedDate.time))
    }

    private fun loadUserProfile() {
        // Mevcut kullanıcının UID'sini al
        val userId = currentUser.uid

        // Users koleksiyonundaki belgeyi al
        val userRef = firestore.collection("Users").document(userId)
        userRef.get().addOnSuccessListener { documentSnapshot ->
            if (documentSnapshot.exists()) {
                // Belge varsa, UserProfile sınıfına dönüştür ve gerekli alanlara yerleştir
                val firstName = documentSnapshot.getString("firstName")
                val lastName = documentSnapshot.getString("lastName")
                val gender = documentSnapshot.getString("gender")
                val date = documentSnapshot.getString("birthDate")


                // EditText'lere doğru sırayla değerleri set et
                Log.d("LoadUserProfile", "First Name: $firstName, Last Name: $lastName, Gender: $gender, Birth Date: $date")
                firstNameEditText.setText(firstName)
                lastNameEditText.setText(lastName)
                genderRadioGroup.check(if (gender == "Erkek") R.id.radioButtonMale else R.id.radioButtonFemale)
                datePicker.setText(date)

                // Profil resmi alanını kullanmak istiyorsanız burada kullanabilirsiniz
            } else {
                Log.d("LoadUserProfile", "Belge bulunamadı")
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Kullanıcı bilgileri alınamadı", Toast.LENGTH_SHORT).show()
            Log.e("LoadUserProfile", "Belge alınırken hata oluştu", it)
        }
    }

    private fun updateUserProfile(updatedUserProfile: UserProfile) {
        // Mevcut kullanıcının UID'sini al
        val userId = currentUser.uid

        // Users koleksiyonundaki belgeyi güncelle
        val userRef = firestore.collection("Users").document(userId)
        userRef.set(updatedUserProfile, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profil güncellendi", Toast.LENGTH_SHORT).show()
                finish() // Aktiviteyi kapat
            }
            .addOnFailureListener {
                Toast.makeText(this, "Profil güncelleme hatası", Toast.LENGTH_SHORT).show()
            }
    }
}