package com.es.inminiapplication.view.home

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.ChildListAdapter
import com.es.inminiapplication.R
import com.es.inminiapplication.VaccineAdapter
import com.es.inminiapplication.model.Child
import com.es.inminiapplication.model.FAQCategory
import com.es.inminiapplication.model.FAQDocument
import com.es.inminiapplication.repository.VaccineViewModelFactory
import com.es.inminiapplication.view.app.VaccineActivity
import com.es.inminiapplication.viewmodel.VaccineViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FaqListActivity : AppCompatActivity() {

    companion object {
        const val CATEGORY_ID = "categoryId"
        const val CATEGORY_NAME = "categoryName"
    }

    //private lateinit var vaccineViewModel: VaccineViewModel
    private lateinit var recyclerView: RecyclerView

    private lateinit var categoryId: String
    private lateinit var categoryName: String
    private lateinit var faqListAdapter: FaqListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faq_list)

        categoryId = intent.getStringExtra(CATEGORY_ID) ?: ""
        categoryName = intent.getStringExtra(CATEGORY_NAME) ?: ""

        supportActionBar?.title = "$CATEGORY_NAME "

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        faqListAdapter = FaqListAdapter(ArrayList()) // Bu satırı ekledim.

        recyclerView.adapter = faqListAdapter

        supportActionBar?.title = intent.getStringExtra(CATEGORY_NAME)

        val categoryId = intent.getStringExtra(CATEGORY_ID)

        categoryId?.let { // let fonksiyonu ile nullable durumu kontrol ediyoruz
            val db = FirebaseFirestore.getInstance()
            Log.i("TAG", "x $categoryId")

            val faqsCollection =
                db.collection("faq_category").document(categoryId).collection("faq")

            faqsCollection.get().addOnSuccessListener { querySnapshot ->
                val faqList: MutableList<FAQDocument> = ArrayList()

                for (document in querySnapshot) {
                    Log.i("TAG", "document id ${document.id}")
                    Log.i("TAG", "document id ${document.data}")
                    val title = document.getString("title")
                    val text = document.getString("text")

                    if (title != null && text != null) {
                        val faqDocument = FAQDocument(title, text)
                        faqList.add(faqDocument)
                    }
                }

                faqListAdapter.updateData(faqList)
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
        }

        faqListAdapter.setOnItemClickListener(object : FaqListAdapter.OnItemClickListener {
            override fun onItemClick(faq: FAQDocument) {
                val intent = Intent(this@FaqListActivity, FAQDetailActivity::class.java)
                intent.putExtra(FAQDetailActivity.TITLE, faq.title)
                intent.putExtra(FAQDetailActivity.TEXT, faq.text)
                startActivity(intent)
            }
        })
    }
}
