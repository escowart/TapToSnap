package com.lab49.taptosnap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lab49.taptosnap.databinding.FragmentMainBinding

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Job Application
 * Tap To Snap
 */
class MainFragment : BaseFragment<FragmentMainBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
}
