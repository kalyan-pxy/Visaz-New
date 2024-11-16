package com.pxy.visaz.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private const val DISPLAY_DATE_FORMAT = "dd-MM-yyyy"

    fun convertToDisplayDate(timestamp: Long): String {
        val date = Date(timestamp) // Convert Long to Date
        val formatter = SimpleDateFormat(DISPLAY_DATE_FORMAT, Locale.getDefault()) // Define format
        return formatter.format(date) // Format the date
    }
}