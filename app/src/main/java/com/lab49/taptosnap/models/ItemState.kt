package com.lab49.taptosnap.models

import android.graphics.Bitmap

/**
 * Created by Edwin S. Cowart on 05 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
sealed class ItemState {
    object Default: ItemState()
    abstract class WithBitmap: ItemState() {
        abstract val bitmap: Bitmap
    }
    class Verifying(override val bitmap: Bitmap): WithBitmap()
    class Incorrect(override val bitmap: Bitmap): WithBitmap()
    class Success(override val bitmap: Bitmap): WithBitmap()
}
