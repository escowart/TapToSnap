package com.lab49.taptosnap.models

import android.graphics.Bitmap

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
data class ItemWithState(
    private val item: Item,
    val index: Int,
    var state: ItemState = ItemState.Default,
    var bitmap: Bitmap? = null
) {
    val id: Int
        get() = item.id
    val name: String
        get() = item.name
}
