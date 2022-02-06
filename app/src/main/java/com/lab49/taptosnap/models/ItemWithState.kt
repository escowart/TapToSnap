package com.lab49.taptosnap.models

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
data class ItemWithState(
    private val item: Item,
    var state: ItemState = ItemState.Default
) {
    val id: Int
        get() = item.id
    val name: String
        get() = item.name
}
