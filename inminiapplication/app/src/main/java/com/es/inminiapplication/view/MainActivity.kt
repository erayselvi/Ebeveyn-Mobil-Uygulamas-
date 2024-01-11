package com.es.inminiapplication.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.es.inminiapplication.databinding.ActivityMainBinding
import com.es.inminiapplication.view.app.AppActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.title = "ebebeveyn.com"
        val view = binding.root
        setContentView(view)
        auth= Firebase.auth
    }

    fun singinClicked(view: View) {
        val email = binding.mailText.text.toString()
        val pass = binding.passText.text.toString()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    val intent = Intent(this@MainActivity, AppActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else {
            Toast.makeText(this, "Hatalı giriş", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun singupClicked(view : View){
        val email=binding.mailText.text.toString()
        val pass=binding.passText.text.toString()
        if(email.isNotEmpty() && pass.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,pass).addOnSuccessListener {
            val intent = Intent(this@MainActivity, PersonalActivity::class.java)

                startActivity(intent)
                finish()

            }.addOnFailureListener {
                 Toast.makeText(this@MainActivity,it.localizedMessage,Toast.LENGTH_LONG).show()
            }
        }
        else{
            Toast.makeText(this,"hatalı giris",Toast.LENGTH_LONG).show()
        }
    }
}