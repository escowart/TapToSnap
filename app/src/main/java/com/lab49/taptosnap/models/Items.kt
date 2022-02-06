package com.lab49.taptosnap.models

import java.io.Serializable

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
data class Items(
    val items: Array<Item>
): Serializable
