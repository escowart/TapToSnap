package com.lab49.taptosnap.ui.main

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.lab49.taptosnap.R
import com.lab49.taptosnap.apis.itemApi
import com.lab49.taptosnap.databinding.DialogActionBinding
import com.lab49.taptosnap.databinding.FragmentMainBinding
import com.lab49.taptosnap.databinding.TileImageBinding
import com.lab49.taptosnap.extensions.encodeAsBase64
import com.lab49.taptosnap.infrastructure.Success
import com.lab49.taptosnap.models.ItemState
import com.lab49.taptosnap.models.ItemWithState
import com.lab49.taptosnap.models.UploadImagePayload
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.ui.components.createDialog
import com.lab49.taptosnap.ui.components.showErrorDialog
import com.lab49.taptosnap.ui.recycler.SpacingItemDecorationOptions
import com.lab49.taptosnap.ui.recycler.setup
import com.lab49.taptosnap.util.DebugLog
import com.lab49.taptosnap.util.formatMS
import java.util.*

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var timer: CountDownTimer
    private lateinit var items: List<ItemWithState>
    private val cameraLauncher = registerForActivityResult(
        CameraActivityContract()
    ) {
        it?.let {
            if (!isSafe) {
                DebugLog.e("Unsafe camera activity callback")
                return@registerForActivityResult
            }
            val item = items[it.index]
            item.state = ItemState.Verifying(it.value)
            binding.tileRecyclerView.adapter?.notifyItemChanged(it.index, item)
            uploadImage(item, it.value.encodeAsBase64())
        }
    }

    private fun uploadImage(item: ItemWithState, image: String) {
        itemApi.uploadImage(UploadImagePayload(item.name, image)) request@{
            if (!isSafe) {
                DebugLog.e("Unsafe uploadImage callback")
                return@request
            }
            val state = item.state as ItemState.WithBitmap
            when (it) {
                is Success -> {
                    item.state = if (it.data.matched) ItemState.Success(state.bitmap) else ItemState.Incorrect(state.bitmap)
                    binding.tileRecyclerView.adapter?.notifyItemChanged(item.index)
                    if (items.all { item -> item.state is ItemState.Success }) {
                        gameDone(won = true)
                    }
                }
                else -> requireActivity().showErrorDialog(
                    error = it,
                    abandon = { item.state = ItemState.Incorrect(state.bitmap) },
                    retry = { uploadImage(item, image) }
                )
            }
        }
    }

    private fun gameDone(won: Boolean) {
        timer.cancel()
        val result = requireActivity().createDialog(DialogActionBinding::bind)
        val binding = result.binding
        val dialog = result.dialog
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)

        binding.title.setText(if (won) R.string.nice_job else R.string.game_over)
        binding.message.setText(if (won) R.string.game_won else R.string.ran_out_of_time)
        binding.leftButton.setText(R.string.restart)
        binding.leftButton.setOnClickListener {
            dialog.dismiss()
            navController.navigate(MainFragmentDirections.toLandingFragment())
        }
        binding.rightButton.setText(R.string.exit)
        binding.rightButton.setOnClickListener {
            dialog.dismiss()
            requireActivity().finish()
        }
        dialog.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        timer = object : CountDownTimer((2 * 60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isSafe) return
                val minutesSeconds = formatMS(millisUntilFinished, "mm:ss")
                binding.timerText.text = getString(R.string.time_format, minutesSeconds)
            }

            override fun onFinish() {
                if (!isSafe) return
                binding.timerText.setText(R.string.time_zero)
                gameDone(won = false)
            }
        }
        timer.start()
        items = MainFragmentArgs
            .fromBundle(requireArguments())
            .items
            .items
            .mapIndexed { index, item -> ItemWithState(item, index) }
        binding.tileRecyclerView.setup(
            items = items,
            inflate = TileImageBinding::inflate,
            orientation = RecyclerView.VERTICAL,
            spanCount = 2,
            spacing = SpacingItemDecorationOptions(
                innerMargin = R.dimen.tile_inner_spacing
            )
        ) { binding, item ->
            binding.tileText.text = item.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            when (val state = item.state) {
                is ItemState.Default -> {
                    binding.root.setOnClickListener { cameraLauncher.launch(item.index) }
                    binding.root.setBackgroundResource(R.drawable.tile_gradient)
                    binding.tileImage.setImageBitmap(null)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tileImage.foreground = null
                    }
                    binding.tileCamera.visibility = View.VISIBLE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
                is ItemState.Verifying -> {
                    binding.root.setOnClickListener(null)
                    binding.root.setBackgroundResource(R.drawable.tile_gradient)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tileImage.foreground = ResourcesCompat.getDrawable(resources, R.drawable.tile_overlay, null)
                    }
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.VISIBLE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
                is ItemState.Incorrect -> {
                    binding.root.setOnClickListener { cameraLauncher.launch(item.index) }
                    binding.root.setBackgroundResource(R.drawable.tile_incorrect)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tileImage.foreground = ResourcesCompat.getDrawable(resources, R.drawable.tile_overlay, null)
                    }
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.VISIBLE
                }
                is ItemState.Success -> {
                    binding.root.setOnClickListener(null)
                    binding.root.setBackgroundResource(R.drawable.tile_success)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        binding.tileImage.foreground = null
                    }
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
            }
        }
        return binding.root
    }
}
