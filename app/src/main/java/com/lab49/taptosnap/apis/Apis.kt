package com.lab49.taptosnap.apis

import androidx.fragment.app.Fragment

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
val Fragment.itemApi: ItemApi
    get() = ItemApi(requireActivity()::runOnUiThread)
