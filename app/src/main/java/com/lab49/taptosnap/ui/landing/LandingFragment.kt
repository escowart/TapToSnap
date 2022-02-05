package com.lab49.taptosnap.ui.landing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lab49.taptosnap.databinding.FragmentLandingBinding
import com.lab49.taptosnap.ui.BaseFragment

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take Home
 * Tap To Snap
 */
class LandingFragment : BaseFragment<FragmentLandingBinding>() {
    override fun onCreateView(
        inflater: LayoutInflater,container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLandingBinding.inflate(inflater, container, false)
        return binding.root
    }
}
