package com.lab49.taptosnap.ui.main

import com.lab49.taptosnap.models.Item

/**
 * Created by Edwin S. Cowart on 05 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
data class ItemAndState(
    private val item: Item,
    val status: ItemState = ItemState.Default
) {
    val id: Int
        get() = item.id
    val name: String
        get() = item.name
}
