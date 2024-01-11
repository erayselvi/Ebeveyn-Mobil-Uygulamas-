package com.es.inminiapplication.repository


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.es.inminiapplication.viewmodel.VaccineViewModel

class VaccineViewModelFactory(private val childId: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VaccineViewModel::class.java)) {
            // VaccineViewModel sınıfıyla eşleşiyorsa, childId parametresini kullanarak bir örneğini oluştur
            @Suppress("UNCHECKED_CAST")
            return VaccineViewModel(childId) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}