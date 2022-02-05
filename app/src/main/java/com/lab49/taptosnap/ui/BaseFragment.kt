package com.lab49.taptosnap.ui

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.viewbinding.ViewBinding
import com.lab49.taptosnap.R
import com.lab49.taptosnap.infrastructure.ApiResponse
import com.lab49.taptosnap.util.DebugLog
import android.content.DialogInterface

import com.lab49.taptosnap.MainActivity
import com.lab49.taptosnap.infrastructure.ClientError
import com.lab49.taptosnap.infrastructure.ServerError


/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
abstract class BaseFragment<Binding : ViewBinding> : Fragment() {
    private var _binding: Binding? = null

    // Valid between onCreateView & onDestroyView.
    protected var binding
        get() = _binding!!
        set(value) { _binding = value }

    protected val isSafe: Boolean
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

    fun errorDialog(response: ApiResponse<*>, retry: (() -> Unit)? = null) {
        if (!isSafe) {
            DebugLog.e("errorDialog($response) while unsafe")
            return
        }
        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Error")
        // TODO - Use the error variant to determine the error message
        alertDialog.setMessage("Oops, Something went wrong!")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK") { dialog, _ ->
            dialog.dismiss()
        }
        retry?.let {
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Retry") { dialog, _ ->
                dialog.dismiss()
                retry()
            }
        }
        alertDialog.show()
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
