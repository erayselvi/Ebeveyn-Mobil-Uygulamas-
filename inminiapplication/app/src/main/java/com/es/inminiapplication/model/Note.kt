package com.es.inminiapplication.model

import com.google.firebase.Timestamp


data class Note(
    val title: String = "",
    val content: String = "",
    val userId: String = "",
    val timestamp: Timestamp = Timestamp.now()
)
