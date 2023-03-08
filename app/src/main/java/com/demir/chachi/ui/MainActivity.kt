package com.demir.chachi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.demir.chachi.R
import com.demir.chachi.databinding.ActivityMainBinding
import com.demir.chachi.viewModel.ProdeuctViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val productViewModel: ProdeuctViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Chachi)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}