package com.lab49.taptosnap.extensions

import android.graphics.Bitmap

import android.util.Base64
import java.io.ByteArrayOutputStream


/**
 * Created by Edwin S. Cowart on 07 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
fun Bitmap.encodeAsBase64(
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.PNG,
    quality: Int = 100
): String {
    val stream = ByteArrayOutputStream()
    compress(format, quality, stream)
    val byteArray: ByteArray = stream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}
