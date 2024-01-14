package com.es.inminiapplication.view.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.Toast
import com.es.inminiapplication.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

import com.github.mikephil.charting.charts.LineChart

import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private lateinit var progressBar: ProgressBar
private lateinit var childDocumentId: String
private lateinit var childName: String
private lateinit var LengthAdd: FloatingActionButton
private lateinit var auth: FirebaseAuth
private lateinit var firestore: FirebaseFirestore
private lateinit var userId: String

private lateinit var lineChart: LineChart

class LengthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_length)

        childDocumentId = intent.getStringExtra(CHILD_DOCUMENT_ID) ?: ""
        childName = intent.getStringExtra(CHILD_NAME) ?: ""
        supportActionBar?.title="${childName} Boy Haritası"
        LengthAdd = findViewById(R.id.LengthAdd)

        lineChart = findViewById(R.id.lineChart)

        auth = Firebase.auth
        firestore = Firebase.firestore


        userId = auth.currentUser?.uid ?: ""

        // Çocuğun boy bilgilerini Firestore'dan al
        fetchAndDisplayLengthData()

        LengthAdd.setOnClickListener {
            val intent = Intent(this, LengthAddActivity::class.java)
            intent.putExtra(LengthAddActivity.CHILD_DOCUMENT_ID, childDocumentId)
            finish()
            startActivity(intent)
        }
    }
    private fun fetchAndDisplayLengthData() {
        // Firestore'dan çocuğun boy bilgilerini çek
        // Firestore sorgusuyla çocuğun boy bilgilerini al ve lineChart'a ekle
        // Firestore sorgusu örnek olarak kullanılmalıdır, gerçek sorgu yapısını projenize uygun şekilde ayarlamalısınız.

        // Firestore sorgusu örneği:
        firestore.collection("Users").document(userId).collection("Children")
            .document(childDocumentId)
            .collection("Lengths")
            .orderBy("date") // Boy bilgilerini tarih sırasına göre sırala
            .get()
            .addOnSuccessListener { documents ->
                Log.i("Boy", "${documents}")
                val entries = mutableListOf<Entry>()

                for ((index, document) in documents.withIndex()) {
                    Log.i("Boy", "${document.data}")

                    val length: Float = when (val lengthData = document["length"]) {
                        is Double -> lengthData.toFloat()
                        is String -> lengthData.toFloatOrNull() ?: 0f
                        else -> 0f
                    }

                    entries.add(Entry(index.toFloat(), length))
                }

                // LineChart için LineDataSet oluştur
                val dataSet = LineDataSet(entries, "Boy Farkı")
                val lineData = LineData(dataSet)

                // xAxis ayarları
                val xAxis = lineChart.xAxis
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.setDrawAxisLine(false) // x eksenindeki çizgiyi kaldır
                xAxis.setDrawGridLines(false) // x eksenindeki gridlines'ları kaldır
                xAxis.setLabelCount(entries.size, true) // x eksenindeki etiket sayısını belirle
                xAxis.textSize = 12f // x eksenindeki yazı boyutunu belirle

                // Grafik ayarları
                lineChart.description.isEnabled = false
                lineChart.data = lineData
                lineChart.invalidate()
            }
            .addOnFailureListener { exception ->
                // Hata durumunda buraya düşer
                hideProgressBar()
                Toast.makeText(this, "Boy bilgileri alınırken hata oluştu.", Toast.LENGTH_SHORT)
                    .show()
            }
    }


        companion object {
        const val CHILD_DOCUMENT_ID = "child_document_id"
        const val CHILD_NAME = "child_name"

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
}

