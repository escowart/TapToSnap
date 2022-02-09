package com.lab49.taptosnap.ui.landing

import android.app.Dialog
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
    private var handler: Handler? = null
    private var dialog: Dialog? = null
    private var dialogTimeout: Runnable? = null
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        binding.letsGoButon.setOnClickListener {
            val result = requireActivity().createDialog(DialogProgressBarBinding::bind)
            dialog = result.dialog
            dialog!!.setCancelable(false)
            dialog!!.setCanceledOnTouchOutside(false)
            dialog!!.window!!.setBackgroundDrawableResource(R.color.bg_gray_dark_70_percent)
            handler = Handler(Looper.getMainLooper())
            dialogTimeout = Runnable runnable@{ dialog?.dismiss() }
            handler!!.postDelayed(dialogTimeout!!, (10 * 1000).toLong())
            dialog!!.show()
            getItems()
        }
        return binding.root
    }

    private fun getItems() {
        itemApi.getItems request@{
            if (!isSafe) {
                DebugLog.e("Unsafe getItems callback")
                return@request
            }
            when (it) {
                is Success -> {
                    dialogTimeout?.let { handler?.removeCallbacks(it) }
                    dialog?.dismiss()
                    navController.navigate(LandingFragmentDirections.toMainFragment(Items(it.data)))
                }
                else -> requireActivity().showErrorDialog(
                    error = it,
                    abandon = {
                        dialogTimeout?.let { handler?.removeCallbacks(it) }
                        dialog?.dismiss()
                    },
                    retry = { getItems() }
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogTimeout?.let { handler?.removeCallbacks(it) }
        dialog?.dismiss()
        dialog = null
        handler = null
        dialogTimeout = null
    }
}
