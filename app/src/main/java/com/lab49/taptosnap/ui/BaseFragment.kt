package com.lab49.taptosnap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.lab49.taptosnap.R
import com.lab49.taptosnap.util.DebugLog

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take Home
 * Tap To Snap
 */
abstract class BaseFragment<Binding : ViewBinding> : Fragment() {
    private var _binding: Binding? = null

    // Valid between onCreateView & onDestroyView.
    protected var binding
        get() = _binding!!
        set(value) { _binding = value }

    private val isSafe: Boolean
        get() = activity != null && isAdded && !isDetached && !isRemoving

    // Valid between onAttach & onDetach
    private val navController: NavController
        get() = requireActivity().findNavController(R.id.nav_host_fragment_content_main)

    fun navigate(directions: NavDirections) {
        if (!isSafe) {
            DebugLog.e("navigate($directions) while unsafe")
            return
        }
        navController.navigate(directions)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        throw NotImplementedError("${javaClass.simpleName} must override onCreateView & inflate the binding")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
