package com.example.studentstudyplanner.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateTimeUtils {

    fun formatDate(timestamp: Long, pattern: String = "MMM dd, yyyy"): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long, pattern: String = "MMM dd, yyyy HH:mm"): String {
        val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
        return dateFormat.format(Date(timestamp))
    }
}
