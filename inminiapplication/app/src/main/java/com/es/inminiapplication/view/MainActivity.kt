package com.es.inminiapplication.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.es.inminiapplication.databinding.ActivityMainBinding
import com.es.inminiapplication.view.app.AppActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var progressBar: ProgressBar

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        supportActionBar?.title = "ebebeveyn.com"
        val view = binding.root

        progressBar = binding.progressBar
        progressBar.visibility = View.INVISIBLE  // Başlangıçta görünmez yapın

        setContentView(view)
        auth = Firebase.auth

        sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        showProgressBar()
        // Eğer kullanıcı daha önce giriş yapmışsa ve hatırla özelliği etkinse, otomatik olarak giriş yap
        if (auth.currentUser != null) {
            hideProgressBar()
            val intent = Intent(this@MainActivity, AppActivity::class.java)
            startActivity(intent)
            finish()  // MainActivity'yi kapat
        } else {
            Log.i("autologin", "else")
            val savedEmail = sharedPreferences.getString("email", "")
            val savedPassword = sharedPreferences.getString("password", "")

            if (savedEmail != null && savedPassword != null && savedEmail.isNotEmpty() && savedPassword.isNotEmpty()) {

                autoLogin(savedEmail, savedPassword)
            }
            hideProgressBar()
        }
    }

    private fun autoLogin(email: String, password: String) {
        Log.i("autologin", "girdi")

        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                hideProgressBar()

                val intent = Intent(this@MainActivity, AppActivity::class.java)
                startActivity(intent)
                finish()  // MainActivity'yi kapat
            }
            .addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
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

    fun singinClicked(view: View) {
        val email = binding.mailText.text.toString()
        val pass = binding.passText.text.toString()

        showProgressBar()

        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, pass)
                .addOnSuccessListener {
                    hideProgressBar()
                    // Kullanıcı giriş yaptığında SharedPreferences'e giriş bilgilerini kaydet
                    sharedPreferences.edit().apply {
                        putString("email", email)
                        putString("password", pass)
                        apply()
                    }

                    val intent = Intent(this@MainActivity, AppActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    hideProgressBar()
                    Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
                }
        } else {
            hideProgressBar()
            Toast.makeText(this, "Hatalı giriş", Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SuspiciousIndentation")
    fun singupClicked(view: View) {

        showProgressBar()

        val email = binding.mailText.text.toString()
        val pass = binding.passText.text.toString()
        if (email.isNotEmpty() && pass.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener {
                // Kullanıcı kaydolduğunda SharedPreferences'e giriş bilgilerini kaydet
                sharedPreferences.edit().apply {
                    putString("email", email)
                    putString("password", pass)
                    apply()
                }
                hideProgressBar()
                val intent = Intent(this@MainActivity, PersonalActivity::class.java)
                startActivity(intent)
                finish()

            }.addOnFailureListener {
                hideProgressBar()
                Toast.makeText(this@MainActivity, it.localizedMessage, Toast.LENGTH_LONG).show()
            }
        } else {
            hideProgressBar()
            Toast.makeText(this, "Hatalı giriş", Toast.LENGTH_LONG).show()
        }
    }
}
