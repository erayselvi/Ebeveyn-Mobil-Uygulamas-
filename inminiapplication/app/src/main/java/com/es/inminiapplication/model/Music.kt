package com.es.inminiapplication.model

import com.google.firebase.storage.StorageReference

data class Music(
    val title: String,
    val storageReference: StorageReference
)