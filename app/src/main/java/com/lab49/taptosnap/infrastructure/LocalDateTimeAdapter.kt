package com.lab49.taptosnap.infrastructure

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
* Created by Edwin S. Cowart
* Lab49 Take-Home
* Tap To Snap
*/
@RequiresApi(Build.VERSION_CODES.O)
class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String {
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }

}
