package com.es.inminiapplication.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

object DateUtils {
    fun getDateFromDateString(dateString: String): Date {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        try {
            return dateFormat.parse(dateString) ?: Date()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return Date()
    }
}