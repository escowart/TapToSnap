package com.lab49.taptosnap.extensions

import java.util.*

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
fun String.pascalToUnderscore(): String {
    return replace("[A-Z]".toRegex()) { " ${it.value.lowercase()}" }
        .trim()
        .replace(" ", "_")
}

fun String.capitalizeFirst(): String {
    // Kotlin deprecated the `capitalize` function in favor of this code
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()
        ) else it.toString()
    }
}
