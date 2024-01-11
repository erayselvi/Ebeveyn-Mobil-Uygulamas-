package com.es.inminiapplication.view

import UserProfile
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.inminiapplication.R
import com.es.inminiapplication.view.app.AppActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class MyActivity : AppCompatActivity() {

    private lateinit var nameTextView: TextView
    private lateinit var birthDateTextView: TextView
    private lateinit var genderTextView: TextView
    private lateinit var profileImageView: ImageView

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        supportActionBar?.title = "ebebeveyn.com"

        nameTextView = findViewById(R.id.nameTextView)
        //birthDateTextView = findViewById(R.id.birthDateTextView)
        //genderTextView = findViewById(R.id.genderTextView)
        profileImageView = findViewById(R.id.profileImageView)

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Kullanıcı bilgilerini çek ve ekrana göster
        loadUserProfile()
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
                    startActivity(Intent(this, MyActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
        }
    }
    private fun loadUserProfile() {
        val currentUser = firebaseAuth.currentUser
        currentUser?.let { user ->
            val userId = user.uid

            val userRef = firestore.collection("users").document(userId)
            userRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val userProfile = documentSnapshot.toObject(UserProfile::class.java)
                    userProfile?.let {
                        val fullName = "${it.firstName.orEmpty()} ${it.lastName.orEmpty()}"
                        nameTextView.text = fullName
                        birthDateTextView.text = "Doğum Tarihi: ${it.birthDate.orEmpty()}"
                        genderTextView.text = "Cinsiyet: ${it.gender.orEmpty()}"

                        // Profil fotoğrafını gösterme
                        loadProfileImage(it.profileImageUrl.orEmpty())
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Kullanıcı bilgileri alınamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

        private fun loadProfileImage(imageUrl: String) {
            if (imageUrl.isNotEmpty()) {
                // Profil fotoğrafını yükleyip gösterme
                val storageRef = storage.getReferenceFromUrl(imageUrl)
                Glide.with(this)
                    .load(storageRef)
                    .placeholder(R.drawable.default_profile_image) // Varsayılan profil fotoğrafı
                    .into(profileImageView)
            } else {
                // Varsayılan profil fotoğrafını gösterme
                profileImageView.setImageResource(R.drawable.default_profile_image)
            }
    }
}