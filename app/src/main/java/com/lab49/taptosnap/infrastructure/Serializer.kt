package com.lab49.taptosnap.infrastructure

import android.os.Build
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Created by Edwin S. Cowart
 * Lab49 Take-Home
 * Tap To Snap
 */
object Serializer {
    @JvmStatic
    val moshiBuilder: Moshi.Builder by lazy {
        val builder = Moshi.Builder()
            .add(UUIDAdapter())
            .add(ByteArrayAdapter())
            .add(URIAdapter())
            .add(KotlinJsonAdapterFactory())
            .add(BigDecimalAdapter())
            .add(BigIntegerAdapter())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.add(OffsetDateTimeAdapter())
                .add(LocalDateTimeAdapter())
                .add(LocalDateAdapter())
        }
        builder
    }

    @JvmStatic
    val moshi: Moshi by lazy {
        moshiBuilder.build()
    }
}
