package com.example.flowcomparison

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.flowcomparison.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        viewModel = MainViewModel()
        setupOnClick()
        suscribe()
        setContentView(binding.root)
    }

    private fun setupOnClick(){
        binding.buttonLiveData.setOnClickListener {
            viewModel.triggerLiveData()
        }

        binding.buttonStateFlow.setOnClickListener {
            viewModel.triggerStateFlow()
        }

        binding.buttonFlow.setOnClickListener {
            lifecycleScope.launch {
                viewModel.triggerFlow().collectLatest {
                    binding.lblFlow.text = it
                }
            }

        }

        binding.buttonSharedFlow.setOnClickListener {
            viewModel.triggerSharedFlow()
        }
    }

    private fun suscribe() {
        viewModel.liveData.observe(this) {
            binding.lblLiveData.text = it
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.lblStateFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.sharedFlow.collectLatest {
                binding.lblSharedFlow.text = it
                Snackbar.make(
                    binding.root,
                    it,
                    Snackbar.LENGTH_LONG
                ).show()
            }
        }
    }
}