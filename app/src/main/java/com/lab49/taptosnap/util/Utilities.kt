package com.lab49.taptosnap.util

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by Edwin S. Cowart on 08 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */

fun formatMS(ms: Long, format: String): String {
    val formatter = SimpleDateFormat(format, Locale.getDefault())
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = ms
    return formatter.format(calendar.time)
}
