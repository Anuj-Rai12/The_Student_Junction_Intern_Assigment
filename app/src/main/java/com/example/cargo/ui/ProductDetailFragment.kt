package com.example.cargo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargo.R
import com.example.cargo.databinding.ProductDescriptionFragmentBinding
import com.example.cargo.paginate.recycle.ProductAdaptor
import com.example.cargo.utils.TAG
import com.example.data.DataProduction
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.product_description_fragment) {
    private lateinit var binding: ProductDescriptionFragmentBinding
    private val list: MutableList<DataProduction> = mutableListOf()
    private val args: ProductDetailFragmentArgs by navArgs()

    @Inject
    lateinit var productAdaptor: ProductAdaptor
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProductDescriptionFragmentBinding.bind(view)
        Log.i(TAG, "onViewCreated: ${args.shop}\n${args.maxprice}")
        setUpRecycle()
        setData()
    }

    private fun setUpRecycle() {
        binding.apply {
            productLayout.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = productAdaptor
            }
        }
    }

    private fun setData() {
        list.add(DataProduction.ImageUri(args.shop.image))
        list.add(DataProduction.ShoppingInfo(args.shop, args.maxprice.toDouble()))
        productAdaptor.submitList(list)
    }
}