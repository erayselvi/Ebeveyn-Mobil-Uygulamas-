package com.es.inminiapplication.view.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.es.inminiapplication.R

class FAQDetailActivity : AppCompatActivity() {

    private lateinit var titleTextView: TextView
    private lateinit var textTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faqdetail)

        titleTextView = findViewById(R.id.titleTextView)
        textTextView = findViewById(R.id.textViewContent)

        val title = intent.getStringExtra(TITLE)
        val text = intent.getStringExtra(TEXT)

        title?.let { titleTextView.text = it }
        text?.let { textTextView.text = it }

        supportActionBar?.title = "ebebeveyn.com"
    }

    companion object {
        const val TITLE = "title"
        const val TEXT = "text"
    }
}
