package com.es.inminiapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.es.inminiapplication.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class AppActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        // BottomNavigationView'daki seçenekleri dinleme
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, AppActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    startActivity(Intent(this, AddNoteActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, AddNoteActivity::class.java))
                    return@setOnNavigationItemSelectedListener true
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
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

}