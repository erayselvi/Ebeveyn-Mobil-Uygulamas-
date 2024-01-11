package com.es.inminiapplication.view

import com.es.inminiapplication.view.my.EditProfileActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.es.inminiapplication.R
import com.es.inminiapplication.view.app.AppActivity
import com.es.inminiapplication.view.my.EditSChildActivity
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
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my)
        nameTextView = findViewById(R.id.nameTextView)
        //birthDateTextView = findViewById(R.id.birthDateTextView)
        //genderTextView = findViewById(R.id.genderTextView)
        profileImageView = findViewById(R.id.profileImageView)
        infoTextView = findViewById(R.id.infoTextView)
        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()


        loadUserProfile()

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        supportActionBar?.title = "ebebeveyn.com"


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
                    image.let{loadProfileImage(it.toString())}

                }
                }
        }
    }

    private fun loadProfileImage(imageUrl: String) {
        if (imageUrl.isNotEmpty()) {
            Log.i("isEmpty?","$imageUrl")
            // Profil fotoğrafını yükleyip gösterme
            val storageRef = storage.getReferenceFromUrl(imageUrl)
            Glide.with(this)
                .load(storageRef)
                .placeholder(R.drawable.default_profile_image) // Varsayılan profil fotoğrafı
                .into(profileImageView)
        } else {
            Log.i("isEmpty?","$imageUrl")
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
}

