package com.es.inminiapplication.repository

import com.es.inminiapplication.model.VaccineInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.firestore.*

class FavoriteVaccinesManager(private val userId: String, private val childId: String) {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val favoriteVaccinesCollection: CollectionReference =
        firestore.collection("Users").document(userId).collection("Children").document(childId).collection("Vaccines")


    fun addFavoriteVaccineInfo(vaccineInfo: VaccineInfo) {
        favoriteVaccinesCollection.document(vaccineInfo.name).set(vaccineInfo)
    }

    fun removeFavoriteVaccineInfo(vaccineInfo: VaccineInfo) {
        favoriteVaccinesCollection.document(vaccineInfo.name).delete()
    }

    fun getChildVaccineInfos(callback: (List<VaccineInfo>) -> Unit) {

        // Koleksiyondan verileri çekin
        favoriteVaccinesCollection.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val vaccineInfos = mutableListOf<VaccineInfo>()

                val result: QuerySnapshot? = task.result
                result?.let {
                    for (document: DocumentSnapshot in it.documents) {

                        val vaccineInfo = document.toObject(VaccineInfo::class.java)
                        vaccineInfo?.let { info -> vaccineInfos.add(info) }
                    }
                }

                // Callback ile elde edilen verileri gönderin
                callback.invoke(vaccineInfos)
            } else {
                // Hata durumunda uygun şekilde işlem yapabilirsiniz
                callback.invoke(emptyList())
            }
        }
    }
}

