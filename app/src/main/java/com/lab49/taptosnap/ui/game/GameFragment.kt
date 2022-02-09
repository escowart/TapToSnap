package com.lab49.taptosnap.ui.game

import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lab49.taptosnap.R
import com.lab49.taptosnap.apis.itemApi
import com.lab49.taptosnap.databinding.DialogActionBinding
import com.lab49.taptosnap.databinding.FragmentGameBinding
import com.lab49.taptosnap.databinding.TileImageBinding
import com.lab49.taptosnap.extensions.capitalizeFirst
import com.lab49.taptosnap.extensions.encodeAsBase64
import com.lab49.taptosnap.extensions.setForegroundDrawable
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
class GameFragment : BaseFragment<FragmentGameBinding>() {
    private var timer: CountDownTimer? = null
    private var handler: Handler? = null
    private var gameWonDelay: Runnable? = null
    private lateinit var items: List<ItemWithState>
    private val gameWon: Boolean
        get() = items.all { item -> item.state is ItemState.Success }
    private var gameAlreadyEnded = false

    private val cameraLauncher = registerForActivityResult(
        CameraActivityContract()
    ) {
        it?.let {
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
                    if (gameWon) {
                        handler = Handler(Looper.getMainLooper())
                        gameWonDelay = Runnable { endOfGame() }
                        // Delay the win by a second so the player has time to see the tile turn green
                        handler!!.postDelayed(gameWonDelay!!, 1000)
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

    @Synchronized
    private fun endOfGame() {
        if (gameAlreadyEnded) return
        gameAlreadyEnded = true
        timer?.cancel()
        gameWonDelay?.let { handler?.removeCallbacks(it) }
        val result = requireActivity().createDialog(DialogActionBinding::bind)
        val binding = result.binding
        val dialog = result.dialog
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawableResource(R.color.transparent)
        // Handle the race condition between the two ways the game ends
        val won = gameWon
        binding.title.setText(if (won) R.string.nice_job else R.string.game_over)
        binding.message.setText(if (won) R.string.game_won else R.string.ran_out_of_time)
        binding.leftButton.setText(R.string.restart)
        binding.leftButton.setOnClickListener {
            dialog.dismiss()
            navController.navigate(GameFragmentDirections.toLandingFragment())
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
        binding = FragmentGameBinding.inflate(inflater, container, false)
        timer = object : CountDownTimer((2 * 60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (!isSafe) return
                val minutesSeconds = formatMS(millisUntilFinished, "mm:ss")
                binding.timerText.text = getString(R.string.time_format, minutesSeconds)
            }

            override fun onFinish() {
                if (!isSafe) return
                binding.timerText.setText(R.string.time_zero)
                endOfGame()
            }
        }
        timer!!.start()
        items = GameFragmentArgs
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
            binding.tileText.text = item.name.capitalizeFirst()
            when (val state = item.state) {
                is ItemState.Default -> {
                    binding.root.setOnClickListener { cameraLauncher.launch(item.index) }
                    binding.root.setBackgroundResource(R.drawable.tile_gradient)
                    binding.tileImage.setImageBitmap(null)
                    binding.tileImage.setForegroundDrawable(null)
                    binding.tileCamera.visibility = View.VISIBLE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
                is ItemState.Verifying -> {
                    binding.root.setOnClickListener(null)
                    binding.root.setBackgroundResource(R.drawable.tile_gradient)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    binding.tileImage.setForegroundDrawable(R.drawable.tile_overlay)
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.VISIBLE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
                is ItemState.Incorrect -> {
                    binding.root.setOnClickListener { cameraLauncher.launch(item.index) }
                    binding.root.setBackgroundResource(R.drawable.tile_incorrect)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    binding.tileImage.setForegroundDrawable(R.drawable.tile_overlay)
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.VISIBLE
                }
                is ItemState.Success -> {
                    binding.root.setOnClickListener(null)
                    binding.root.setBackgroundResource(R.drawable.tile_success)
                    binding.tileImage.setImageBitmap(state.bitmap)
                    binding.tileImage.setForegroundDrawable(null)
                    binding.tileCamera.visibility = View.GONE
                    binding.tileSpinner.visibility = View.GONE
                    binding.tileTapToTryAgainText.visibility = View.GONE
                }
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timer?.cancel()
        gameWonDelay?.let { handler?.removeCallbacks(it) }
        handler = null
        gameWonDelay = null
        timer = null
    }
}
