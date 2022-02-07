package com.lab49.taptosnap.extensions

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
