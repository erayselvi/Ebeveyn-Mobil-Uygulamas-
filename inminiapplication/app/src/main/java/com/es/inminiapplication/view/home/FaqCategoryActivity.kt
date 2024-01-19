package com.es.inminiapplication.view.home

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.R
import com.es.inminiapplication.adapter.FaqCategoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.es.inminiapplication.model.FAQCategory

class FaqCategoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var faqCategoryAdapter: FaqCategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children_list)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        faqCategoryAdapter = FaqCategoryAdapter(ArrayList())
        recyclerView.adapter = faqCategoryAdapter

        supportActionBar?.title = "Kategoriler"

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val faqCategoriesCollection = db.collection("faq_category")

            faqCategoriesCollection.get().addOnSuccessListener { querySnapshot ->
                val faqCategoryList: MutableList<FAQCategory> = ArrayList()

                for (document in querySnapshot) {
                    Log.i("TAG","document id ${document.id}")
                    Log.i("TAG","document id ${document.data}")

                    val categoryName = document.getString("categoryName")
                    val firestoreId = document.id

                    if (categoryName != null) {
                        val faqCategory = FAQCategory(categoryName, firestoreId)
                        faqCategoryList.add(faqCategory)
                    }
                }

                faqCategoryAdapter.updateData(faqCategoryList)
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
        }

        faqCategoryAdapter.setOnItemClickListener(object : FaqCategoryAdapter.OnItemClickListener {
            override fun onItemClick(faqCategory: FAQCategory) {
                val intent = Intent(this@FaqCategoryActivity, FaqListActivity::class.java)
                intent.putExtra(FaqListActivity.CATEGORY_ID, faqCategory.firestoreId)
                intent.putExtra(FaqListActivity.CATEGORY_NAME, faqCategory.categoryName)
                startActivity(intent)
            }
        })
    }
}
