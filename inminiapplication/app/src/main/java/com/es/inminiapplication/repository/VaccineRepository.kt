package com.es.inminiapplication.repository

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VaccineRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    fun saveVaccineStatus(month: Int, vaccineName: String, isChecked: Boolean) {
        val vaccineRef = database.child("vaccines").child(month.toString()).child("vaccineList").child(vaccineName)
        vaccineRef.child("isChecked").setValue(isChecked)
    }

    fun getVaccineStatus(month: Int, vaccineName: String, callback: (Boolean) -> Unit) {
        val vaccineRef = database.child("vaccines").child(month.toString()).child("vaccineList").child(vaccineName)
        vaccineRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isChecked = snapshot.child("isChecked").getValue(Boolean::class.java) ?: false
                callback(isChecked)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error
            }
        })
    }
}