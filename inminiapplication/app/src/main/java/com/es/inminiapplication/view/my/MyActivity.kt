package com.es.inminiapplication.view.my

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.es.inminiapplication.R
import com.es.inminiapplication.view.MainActivity
import com.es.inminiapplication.view.app.AppActivity
import com.es.inminiapplication.view.home.HomeActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MyActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var birthDateTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var profileImageView: ImageView
    private lateinit var infoTextView: TextView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
        nameTextView = findViewById(R.id.nameTextView)
        //birthDateTextView = findViewById(R.id.birthDateTextView)
        //genderTextView = findViewById(R.id.genderTextView)
        profileImageView = findViewById(R.id.profileImageView)
        infoTextView = findViewById(R.id.infoTextView)

        firebaseAuth = FirebaseAuth.getInstance()
        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        progressBar = findViewById(R.id.progressBar)
        progressBar.visibility = View.INVISIBLE


        loadUserProfile()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        supportActionBar?.title = "ebebeveyn.com"

        val exitButton = findViewById<ImageView>(R.id.exitButton)
        exitButton.setOnClickListener {
            exitButtonClicked()
        }

        // Kullanıcı bilgilerini çek ve ekrana göster

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, AppActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    // Burada zaten MyActivity'e geçiş yaptığınız için bir şey yapmanıza gerek yok.
                    return@setOnNavigationItemSelectedListener true
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
        bottomNavigationView.selectedItemId = R.id.navigation_notifications

    }


    private fun loadUserProfile() {
        showProgressBar()

        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            val userRef = firestore.collection("Users").document(userId)

            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // Belgedeki alanlara erişim
                    val firstName = documentSnapshot.getString("firstName")
                    val lastName = documentSnapshot.getString("lastName")
                    val gender = documentSnapshot.getString("gender")
                    val date = documentSnapshot.getString("birthDate")
                    val image = documentSnapshot.getString("profileImageUrl")

                    // UI'yi güncelleme
                    nameTextView.text = "$firstName $lastName"
                    infoTextView.text = "$gender | $date"
                    Log.i("TAG","$image")
                    image.let { loadProfileImage(it.toString()) }
                }

                hideProgressBar()
            }.addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this@MyActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.INVISIBLE
    }

    private fun loadProfileImage(imagePath: String) {
        val storageRef = storage.getReferenceFromUrl(imagePath)

        storageRef.downloadUrl
            .addOnSuccessListener { uri ->
                // İndirme URL'sini başarıyla aldık
                val imageUrl = uri.toString()
                Log.i("Download URL", imageUrl)

                // Glide ile resmi gösterme
                Glide.with(this)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.default_profile_image)
                    .into(profileImageView)
            }
            .addOnFailureListener { exception ->
                // İndirme URL'sini alırken hata oluştu
                Log.e("Download URL", "Hata: ${exception.message}", exception)

                // Varsayılan profil fotoğrafını gösterme
                profileImageView.setImageResource(R.drawable.default_profile_image)
            }
    }

    fun editprofile(view : View){
        val intent = Intent(this@MyActivity, EditProfileActivity::class.java)
        startActivity(intent)
    }
    fun editchild(view: View){
        val intent = Intent(this@MyActivity, EditSChildActivity::class.java)
        startActivity(intent)
    }

    fun exitButtonClicked() {
        // FirebaseAuth ile çıkış yap
        firebaseAuth.signOut()

        // SharedPreferences'teki otomatik giriş bilgilerini temizle
        sharedPreferences.edit().clear().apply()

        // Giriş ekranına yönlendir
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finishAffinity()  // Şu anki aktiviteyi kapat
    }
}

