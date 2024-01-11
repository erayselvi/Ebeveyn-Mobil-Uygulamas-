package com.es.inminiapplication.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.cardview.widget.CardView
import com.es.inminiapplication.R
import com.es.inminiapplication.view.home.FaqCategoryActivity

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val cardFAQ: CardView = findViewById(R.id.cardFAQ)
        val cardAllQuestions: CardView = findViewById(R.id.cardAllQuestions)
        val cardMyQuestions: CardView = findViewById(R.id.cardMyQuestions)
        val cardAskQuestion: CardView = findViewById(R.id.cardAskQuestion)

        supportActionBar?.title = "                           Sorular"


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

}