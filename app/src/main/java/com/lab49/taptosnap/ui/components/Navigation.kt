package com.lab49.taptosnap.ui.components

import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.lab49.taptosnap.R
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.util.DebugLog

/**
 * Created by Edwin S. Cowart on 06 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */

// Valid between onAttach & onDetach
private val BaseFragment<*>.navController: NavController
    get() = requireActivity().findNavController(R.id.nav_host_fragment_content_main)

fun BaseFragment<*>.navigate(directions: NavDirections) {
    if (!isSafe) {
        DebugLog.e("Unsafe call to navigate")
        return
    }
    navController.navigate(directions)
}
