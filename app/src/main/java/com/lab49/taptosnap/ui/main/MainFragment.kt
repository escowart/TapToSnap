package com.lab49.taptosnap.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.lab49.taptosnap.apis.ItemApi
import com.lab49.taptosnap.databinding.FragmentMainBinding
import com.lab49.taptosnap.databinding.TileImageBinding
import com.lab49.taptosnap.infrastructure.Success
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.ui.view.newAdapter
import com.lab49.taptosnap.util.DebugLog
import java.io.File

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var itemApi: ItemApi


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        itemApi = ItemApi(requireActivity()::runOnUiThread)

//        binding.tileRecyclerView.newAdapter(items, TileImageBinding::inflate) { binding, item ->
//            // TODO
//        }
        binding.tileRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        return binding.root
    }

    private fun uploadImageRequest(imageLabel: String, image: File) {
        itemApi.uploadImage(imageLabel, image) {
            if (!isSafe) {
                DebugLog.e("Cannot execute uploadImage callback while unsafe")
                return@uploadImage
            }
            when (it) {
                is Success -> {
                    it.data.matched
                }
                else -> errorDialog(response = it, retry = { uploadImageRequest(imageLabel, image) })
            }
        }
    }
}

