package com.lab49.taptosnap.infrastructure

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
* Created by Edwin S. Cowart
* Lab49 Take-Home
* Tap To Snap
*/
@RequiresApi(Build.VERSION_CODES.O)
class LocalDateAdapter {
    @ToJson
    fun toJson(value: LocalDate): String {
        return DateTimeFormatter.ISO_LOCAL_DATE.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDate {
        return LocalDate.parse(value, DateTimeFormatter.ISO_LOCAL_DATE)
    }

}
