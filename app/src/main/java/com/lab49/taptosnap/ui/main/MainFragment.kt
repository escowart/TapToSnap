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
import java.io.File

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var timer: CountDownTimer
    private lateinit var items: List<ItemWithState>

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
        items = MainFragmentArgs.fromBundle(requireArguments()).items.items.map {
            ItemWithState(it)
        }
        binding.tileRecyclerView.setup(
            items = items,
            inflate = TileImageBinding::inflate,
            orientation = RecyclerView.VERTICAL,
            spanCount = 2,
            spacing = SpacingItemDecorationOptions(
                innerMargin = R.dimen.tile_inner_spacing
            )
        ) { binding, item ->
            binding.tileText.text = item.name
            binding.root.setOnClickListener {
                if (item.state !in arrayOf(ItemState.Success, ItemState.Verifying)) return@setOnClickListener

            }
        }
        binding.tileRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        return binding.root
    }

    private fun uploadImageRequest(imageLabel: String, image: File) {
        itemApi.uploadImage(imageLabel, image) {
            if (!isSafe) {
                DebugLog.e("Unsafe uploadImage callback")
                return@uploadImage
            }
            when (it) {
                is Success -> {
                    it.data.matched
                }
                else -> showErrorDialog(error = it, retry = { uploadImageRequest(imageLabel, image) })
            }
        }
    }
}

