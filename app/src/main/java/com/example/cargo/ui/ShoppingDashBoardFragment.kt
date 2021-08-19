package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cargo.R
import com.example.cargo.databinding.ProductScreenFragmentBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingDashBoardFragment :Fragment(R.layout.product_screen_fragment){
    private lateinit var binding: ProductScreenFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding= ProductScreenFragmentBinding.bind(view)

    }
}