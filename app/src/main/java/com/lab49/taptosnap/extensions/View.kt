package com.lab49.taptosnap.extensions

import android.content.res.Resources
import android.os.Build
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

/**
 * Created by Edwin S. Cowart on 09 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */

fun View.setForegroundDrawable(@DrawableRes id: Int?, theme: Resources.Theme? = null) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        foreground = id?.let { ResourcesCompat.getDrawable(resources, id, theme) }
    }
}
