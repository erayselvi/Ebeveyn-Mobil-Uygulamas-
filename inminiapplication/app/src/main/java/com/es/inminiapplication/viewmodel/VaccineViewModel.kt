package com.es.inminiapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.es.inminiapplication.model.VaccineInfo
import com.es.inminiapplication.repository.FavoriteVaccinesManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class VaccineViewModel(childId: String) : ViewModel() {

    private val _vaccineInfos = MutableLiveData<List<VaccineInfo>>()
    val vaccineInfos: LiveData<List<VaccineInfo>> get() = _vaccineInfos

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""

    private val favoriteVaccinesManager = FavoriteVaccinesManager(userId,childId)

    init {
        loadFavoriteVaccineInfos()
    }

    private fun loadFavoriteVaccineInfos() {
        favoriteVaccinesManager.getChildVaccineInfos { favoriteVaccineInfos ->
            _vaccineInfos.postValue(favoriteVaccineInfos)
        }
    }
}
