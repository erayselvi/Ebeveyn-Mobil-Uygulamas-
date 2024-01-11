package com.es.inminiapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.es.inminiapplication.R
import com.es.inminiapplication.view.app.AppActivity
import com.es.inminiapplication.view.home.FaqCategoryActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val cardFAQ: CardView = findViewById(R.id.cardFAQ)
        val cardAllQuestions: CardView = findViewById(R.id.cardAllQuestions)
        val cardMyQuestions: CardView = findViewById(R.id.cardMyQuestions)
        val cardAskQuestion: CardView = findViewById(R.id.cardAskQuestion)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigation)

        supportActionBar?.title = "                           Sorular"
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, AppActivity::class.java))

                }
                R.id.navigation_dashboard -> {
                    //startActivity(Intent(this, HomeActivity::class.java))
                }
                R.id.navigation_notifications -> {
                    startActivity(Intent(this, MyActivity::class.java))
                }
                // Diğer menü öğeleri için aynı şekilde devam edebilirsiniz
                else -> return@setOnNavigationItemSelectedListener false
            }
            return@setOnNavigationItemSelectedListener true
        }
        bottomNavigationView.selectedItemId = R.id.navigation_dashboard
        // Sık Sorulan Sorular Kartı

        cardFAQ.setOnClickListener {
            // Burada Sık Sorulan Sorular ile ilgili bir şey yapabilirsiniz.
            // Örneğin, yeni bir aktivite başlatabilirsiniz:
            val intent = Intent(this, FaqCategoryActivity::class.java)
            startActivity(intent)

        }

        // Tüm Sorular Kartı
        cardAllQuestions.setOnClickListener {
            // Burada Tüm Sorular ile ilgili bir şey yapabilirsiniz.
        }

        // Sorularım Kartı
        cardMyQuestions.setOnClickListener {
            // Burada Sorularım ile ilgili bir şey yapabilirsiniz.
        }

        // Yeni Soru Sor Kartı
        cardAskQuestion.setOnClickListener {
            // Burada Yeni Soru Sor ile ilgili bir şey yapabilirsiniz.
        }
    }



    // BottomNavigationView'daki seçenekleri dinleme

}