package com.lab49.taptosnap.extensions

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.viewbinding.ViewBinding

/**
 * Created by Edwin S. Cowart on 07 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
@LayoutRes
inline fun <reified BINDING: ViewBinding> Context.getLayoutFromBinding(): Int {
    val layout = BINDING::class.java.simpleName
        .replace("Binding", "")
        .pascalToUnderscore()
    return resources.getIdentifier(layout, "layout", packageName)
}
