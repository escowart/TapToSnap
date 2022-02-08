package com.lab49.taptosnap.ui.main

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lab49.taptosnap.R
import com.lab49.taptosnap.apis.itemApi
import com.lab49.taptosnap.databinding.FragmentMainBinding
import com.lab49.taptosnap.databinding.TileImageBinding
import com.lab49.taptosnap.infrastructure.Success
import com.lab49.taptosnap.models.ItemState
import com.lab49.taptosnap.models.ItemWithState
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.ui.components.showErrorDialog
import com.lab49.taptosnap.ui.recycler.SpacingItemDecorationOptions
import com.lab49.taptosnap.ui.recycler.setup
import com.lab49.taptosnap.util.DebugLog

import com.lab49.taptosnap.extensions.encodeAsBase64
import com.lab49.taptosnap.models.UploadImagePayload

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
            uploadImageRequest(item, it.value.encodeAsBase64())
            item.bitmap = it.value
            item.state = ItemState.Verifying
            binding.tileRecyclerView.adapter?.notifyItemChanged(it.index, item)
            DebugLog.e("notifyItemChanged(index=${it.index}, state=${item.state})")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        timer = object : CountDownTimer((15 * 60 * 1000).toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //Some code
            }

            override fun onFinish() {

            }
        }
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
            DebugLog.e("update(index=${item.index}, state=${item.state})")
            binding.tileText.text = item.name
            // TODO - image has to rounded
            // TODO - background actually overlays the image (
            binding.root.setOnClickListener {
                if (item.state in arrayOf(ItemState.Success, ItemState.Verifying)) return@setOnClickListener
                cameraLauncher.launch(item.index)
            }
            item.bitmap?.let {
                binding.tileImage.setImageBitmap(item.bitmap)
            }
            binding.root.setBackgroundResource(when (item.state) {
                ItemState.Default -> R.drawable.tile_default
                ItemState.Verifying -> R.drawable.tile_verifying
                ItemState.Incorrect -> R.drawable.tile_incorrect
                ItemState.Success -> R.drawable.tile_success
            })
        }
        binding.tileRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        return binding.root
    }

    private fun uploadImageRequest(item: ItemWithState, image: String) {
        itemApi.uploadImage(UploadImagePayload(item.name, image)) {
            if (!isSafe) {
                DebugLog.e("Unsafe uploadImage callback")
                return@uploadImage
            }
            when (it) {
                is Success -> {
                    item.state = if (it.data.matched) ItemState.Success else ItemState.Incorrect
                }
                else -> showErrorDialog(
                    error = it,
                    abandon = { item.state = ItemState.Incorrect },
                    retry = { uploadImageRequest(item, image) }
                )
            }
        }
    }
}

