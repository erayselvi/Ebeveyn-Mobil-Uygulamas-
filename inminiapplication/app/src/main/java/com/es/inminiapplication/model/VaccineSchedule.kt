package com.es.inminiapplication.model

data class VaccineSchedule(
    val month: Int,
    val vaccineList: List<VaccineItem>
)

