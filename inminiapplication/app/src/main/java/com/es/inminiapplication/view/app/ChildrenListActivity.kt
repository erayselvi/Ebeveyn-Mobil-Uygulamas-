package com.es.inminiapplication.view.app


import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.ChildListAdapter
import com.es.inminiapplication.R
import com.es.inminiapplication.model.Child
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChildrenListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var childAdapter: ChildListAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_children_list)

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
                Log.d(TAG, "Error getting documents: ", exception)
            }
        }
        childAdapter.setOnItemClickListener(object : ChildListAdapter.OnItemClickListener {
            override fun onItemClick(child: Child) {
                val intent = Intent(this@ChildrenListActivity, VaccineActivity::class.java)
                intent.putExtra(VaccineActivity.CHILD_DOCUMENT_ID, child.firestoreId)
                intent.putExtra(VaccineActivity.CHILD_NAME, child.name)
                startActivity(intent)
            }
        })
    }
}