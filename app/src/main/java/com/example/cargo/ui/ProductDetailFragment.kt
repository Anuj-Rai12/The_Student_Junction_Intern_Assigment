package com.example.cargo.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.cargo.R
import com.example.cargo.databinding.ProductDescriptionFragmentBinding

class ProductDetailFragment : Fragment(R.layout.product_description_fragment) {
    private lateinit var binding: ProductDescriptionFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProductDescriptionFragmentBinding.bind(view)
    }
}