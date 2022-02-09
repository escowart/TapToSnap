package com.lab49.taptosnap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lab49.taptosnap.databinding.ActivityMainBinding

/**
 * Created by Edwin S. Cowart on 04 February, 2022
 * Lab49 Take-Home
 * Tap To Snap
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}