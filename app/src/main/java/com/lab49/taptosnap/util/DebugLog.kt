package com.lab49.taptosnap.util

import android.util.Log
import com.lab49.taptosnap.BuildConfig

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
object DebugLog {
    private val excludeTags = arrayOf(
        "dalvik.system.VMStack",
        "java.lang.Thread",
        "com.lab49.taptosnap.util.DebugLog"
    )
    private val callerTag: String
        get() = Thread
            .currentThread()
            .stackTrace
            .map { it.className }
            .first { !excludeTags.contains(it) }

    private fun log(f: (tag: String?, message: String) -> Unit, vararg messages: Any) {
        if (BuildConfig.DEBUG) {
            f(callerTag, messages.joinToString())
        }
    }
    fun e(vararg messages: Any) = log(Log::e, *messages)
}