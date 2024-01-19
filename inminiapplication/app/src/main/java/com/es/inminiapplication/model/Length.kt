package com.es.inminiapplication.model

import java.util.Date

data class Length(
    val length: Float, // Sayısal değer olarak tutmak istediğiniz için Float kullandım, eğer başka bir sayısal tip kullanmak istiyorsanız onu tercih edebilirsiniz
    val date: Date
)