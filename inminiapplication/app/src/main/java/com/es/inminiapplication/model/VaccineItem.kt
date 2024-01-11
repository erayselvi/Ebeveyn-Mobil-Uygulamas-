package com.es.inminiapplication.model

data class VaccineItems(
    val name: String,
    val description: String,
    val dose: Int,
    val date: String,
    val isTaken: Boolean
)