package com.lab49.taptosnap.ui.landing

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lab49.taptosnap.R
import com.lab49.taptosnap.apis.itemApi
import com.lab49.taptosnap.databinding.DialogProgressBarBinding
import com.lab49.taptosnap.databinding.FragmentLandingBinding
import com.lab49.taptosnap.infrastructure.Success
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.models.Items
import com.lab49.taptosnap.ui.components.createDialog
import com.lab49.taptosnap.ui.components.showErrorDialog
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
        binding.letsGoButon.setOnClickListener { getItems() }
        return binding.root
    }

    private fun getItems() {
        val result = requireActivity().createDialog(DialogProgressBarBinding::bind)
        val dialog = result.dialog
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawableResource(R.color.bg_gray_dark_70_percent)
        Handler(Looper.getMainLooper()).postDelayed({ dialog.dismiss() }, 10000 /* 10 sec */)
        dialog.show()
        itemApi.getItems request@{
            if (!isSafe) {
                DebugLog.e("Unsafe getItems callback")
                return@request
            }
            when (it) {
                is Success -> {
                    dialog.dismiss()
                    navController.navigate(LandingFragmentDirections.toMainFragment(Items(it.data)))
                }
                else -> requireActivity().showErrorDialog(
                    error = it,
                    abandon = { dialog.dismiss() },
                    retry = { getItems() }
                )
            }
        }
    }
}
