package com.es.inminiapplication.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.es.inminiapplication.R

class BabyHeightActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_baby_height)

        val buttonCalculate: Button = findViewById(R.id.buttonCalculate)
        val editTextMotherHeight: EditText = findViewById(R.id.editTextMotherHeight)
        val editTextFatherHeight: EditText = findViewById(R.id.editTextFatherHeight)
        val textViewPrediction: TextView = findViewById(R.id.textViewPrediction)

        buttonCalculate.setOnClickListener {
            val motherHeight = editTextMotherHeight.text.toString().toDoubleOrNull()
            val fatherHeight = editTextFatherHeight.text.toString().toDoubleOrNull()

            if (motherHeight != null && fatherHeight != null) {
                val averageHeight = (motherHeight + fatherHeight) / 2
                val minHeight = averageHeight - 10
                val maxHeight = averageHeight + 10

                val genderPrediction = if (averageHeight < 160) "Kız" else "Erkek"
                val predictionText = "Tahmin Edilen Cinsiyet: $genderPrediction\n" +
                        "Tahmin Edilen Boy Aralığı: $minHeight - $maxHeight cm"

                textViewPrediction.text = predictionText
            } else {
                textViewPrediction.text = "Lütfen geçerli boy değerleri girin."
            }
        }
    }
}