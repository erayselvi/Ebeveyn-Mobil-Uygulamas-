package com.es.inminiapplication.model

data class VaccineInfo(
    val name: String,
    val dose: Int,
    val description: String,
    var date: String,
    var isTaken: Boolean
)