package com.lab49.taptosnap.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.lab49.taptosnap.databinding.FragmentMainBinding
import com.lab49.taptosnap.databinding.TileImageBinding
import com.lab49.taptosnap.ui.BaseFragment
import com.lab49.taptosnap.ui.view.newAdapter

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Job Application
 * Tap To Snap
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private lateinit var model: MainViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        model = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.tileRecyclerView.newAdapter(model.items, TileImageBinding::inflate) { binding, item ->
            // TODO
        }
        return binding.root
    }
}

