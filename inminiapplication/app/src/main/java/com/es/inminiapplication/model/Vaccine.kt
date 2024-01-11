package com.es.inminiapplication.model

data class VaccineModel(
    val month: Int,
    val vaccineList: List<VaccineItem>
)

data class VaccineItem(
    val name: String,
    val dose: Int
)