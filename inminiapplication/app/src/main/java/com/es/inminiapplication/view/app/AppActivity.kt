package com.es.inminiapplication.view.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.es.inminiapplication.R
import com.es.inminiapplication.view.home.HomeActivity
import com.es.inminiapplication.view.my.MyActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)
        supportActionBar?.title = "ebebeveyn.com"


        // BottomNavigationView'daki seçenekleri dinleme
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Burada bir şey yapmanıza gerek yok, çünkü zaten AppActivity açık
                }
                R.id.navigation_dashboard -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, MyActivity::class.java))
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }
    }
    fun bookclicked(view : View){
        val intent = Intent(this@AppActivity, NotesActivity::class.java)
        startActivity(intent)
    }
    fun babyheightclicked(view : View){
        val intent = Intent(this@AppActivity, BabyHeightActivity::class.java)
        startActivity(intent)
    }
    fun vaccinationclicked(view: View){
        val intent = Intent(this@AppActivity, ChildrenListActivity::class.java)
        startActivity(intent)
    }
    fun addchildclicked(view: View){
        val intent = Intent(this@AppActivity, AddChildActivity::class.java)
        startActivity(intent)
    }
    fun lullablyclicked(view: View){
        val intent = Intent(this@AppActivity, LullablyActivity::class.java)
        startActivity(intent)
    }
    fun lengthclicked(view: View){
        val intent = Intent(this@AppActivity, LengthSChildActivity::class.java)
        startActivity(intent)
    }


}