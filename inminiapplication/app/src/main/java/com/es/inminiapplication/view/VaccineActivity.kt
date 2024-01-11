package com.es.inminiapplication.view

import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.es.inminiapplication.R
import com.es.inminiapplication.VaccineAdapter
import com.es.inminiapplication.repository.VaccineRepository
import com.es.inminiapplication.repository.VaccineViewModelFactory
import com.es.inminiapplication.viewmodel.VaccineViewModel

class VaccineActivity : AppCompatActivity() {

    private lateinit var vaccineViewModel: VaccineViewModel
    private lateinit var recyclerView: RecyclerView

    companion object {
        const val CHILD_DOCUMENT_ID = "child_document_id"
        const val CHILD_NAME = "child_name"
    }

    private lateinit var childDocumentId: String
    private lateinit var childName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vaccine)

        childDocumentId = intent.getStringExtra(CHILD_DOCUMENT_ID) ?: ""
        childName = intent.getStringExtra(CHILD_NAME) ?: ""

        supportActionBar?.title = childName + "/Aşı Takvimi"


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // VaccineViewModel'i oluştururken childId'yi gönderin
        vaccineViewModel = ViewModelProvider(this, VaccineViewModelFactory(childDocumentId)).get(VaccineViewModel::class.java)


        vaccineViewModel.vaccineInfos.observe(this) { favoriteVaccineInfos ->
            val adapter = VaccineAdapter(favoriteVaccineInfos)
            recyclerView.adapter = adapter
        }
    }
}
