package com.lab49.taptosnap.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lab49.taptosnap.apis.itemApi
import com.lab49.taptosnap.databinding.FragmentLandingBinding
import com.lab49.taptosnap.infrastructure.Success
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.models.Items
import com.lab49.taptosnap.ui.components.navigate
import com.lab49.taptosnap.ui.components.showErrorDialog
import com.lab49.taptosnap.ui.components.showProgressSpinnerDialog
import com.lab49.taptosnap.util.DebugLog

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class LandingFragment : BaseFragment<FragmentLandingBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        binding.letsGoButon.setOnClickListener { getItemsRequest() }
        return binding.root
    }

    private fun getItemsRequest() {
        val dialog = requireActivity().showProgressSpinnerDialog()
        itemApi.getItems {
            if (!isSafe) {
                DebugLog.e("Unsafe getItems callback")
                return@getItems
            }
            when (it) {
                is Success -> {
                    dialog.dismiss()
                    navigate(LandingFragmentDirections.toMainFragment(Items(it.data)))
                }
                else -> requireActivity().showErrorDialog(
                    error = it,
                    abandon = { dialog.dismiss() },
                    retry = { getItemsRequest() }
                )
            }
        }
    }
}
