package com.lab49.taptosnap.infrastructure

import android.os.Build
import androidx.annotation.RequiresApi
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

/**
* Created by Edwin S. Cowart
* Lab49 Take-Home
* Tap To Snap
*/
@RequiresApi(Build.VERSION_CODES.O)
class OffsetDateTimeAdapter {
    @ToJson
    fun toJson(value: OffsetDateTime): String {
        return DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value)
    }

    @FromJson
    fun fromJson(value: String): OffsetDateTime {
        return OffsetDateTime.parse(value, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }

}
