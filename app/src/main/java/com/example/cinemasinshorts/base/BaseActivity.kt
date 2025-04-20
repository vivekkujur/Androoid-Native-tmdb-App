package com.example.cinemasinshorts.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleOwner

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        setupViews()
        setupObservers()
    }

    abstract fun getLayoutId(): Int
    abstract fun setupViews()
    abstract fun setupObservers()

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
        _binding = null
    }
} 