package com.lab49.taptosnap.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.activity.result.contract.ActivityResultContract

import android.provider.MediaStore


class CameraActivityContract : ActivityResultContract<Int, IndexedValue<Bitmap>?>() {
    private var lastIndex: Int = -1
    override fun createIntent(context: Context, index: Int): Intent {
        lastIndex = index
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE)
    }
    override fun parseResult(resultCode: Int, intent: Intent?): IndexedValue<Bitmap>? = when {
        resultCode != Activity.RESULT_OK -> null
        else -> intent?.extras?.let {
            IndexedValue(index = lastIndex, it.get("data") as Bitmap)
        }
    }
}
