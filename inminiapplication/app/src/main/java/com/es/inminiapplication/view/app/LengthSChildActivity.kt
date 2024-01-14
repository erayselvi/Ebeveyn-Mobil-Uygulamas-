package com.es.inminiapplication.view.app


import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.ChildListAdapter
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Child
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LengthSChildActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var childAdapter: ChildListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children_list)
        supportActionBar?.title = "Çocuklarım"
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        childAdapter = ChildListAdapter(ArrayList())
        recyclerView.adapter = childAdapter

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val db = FirebaseFirestore.getInstance()
            val childrenCollection = db.collection("Users").document(user.uid).collection("Children")

            childrenCollection.get().addOnSuccessListener { querySnapshot ->
                val childList: MutableList<Child> = ArrayList()

                for (document in querySnapshot) {
                    val childName = document.getString("name")
                    val firestoreId = document.id
                    if (childName != null) {
                        val child = Child(childName, firestoreId)
                        childList.add(child)
                    }
                }
                childAdapter.updateData(childList)
            }.addOnFailureListener { exception ->
                Log.d(ContentValues.TAG, "Error getting documents: ", exception)
            }
        }
        childAdapter.setOnItemClickListener(object : ChildListAdapter.OnItemClickListener {
            override fun onItemClick(child: Child) {
                val intent = Intent(this@LengthSChildActivity, LengthActivity::class.java)
                intent.putExtra(LengthActivity.CHILD_DOCUMENT_ID, child.firestoreId)
                intent.putExtra(LengthActivity.CHILD_NAME, child.name)
                startActivity(intent)
                finish()
            }
        })
    }
}