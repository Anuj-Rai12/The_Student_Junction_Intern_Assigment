package com.example.cargo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargo.R
import com.example.cargo.databinding.ProductDescriptionFragmentBinding
import com.example.cargo.paginate.recycle.ProductAdaptor
import com.example.cargo.utils.CustomProgress
import com.example.cargo.utils.ExtraFile
import com.example.cargo.utils.TAG
import com.example.data.DataProduction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.product_description_fragment) {
    private lateinit var binding: ProductDescriptionFragmentBinding
    private val list: MutableList<DataProduction> = mutableListOf()
    private val args: ProductDetailFragmentArgs by navArgs()

    @Inject
    lateinit var productAdaptor: ProductAdaptor

    @Inject
    lateinit var customProgress: CustomProgress

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProductDescriptionFragmentBinding.bind(view)
        Log.i(TAG, "onViewCreated: ${args.shop}\n${args.maxprice}")
        setUpRecycle()
        setData()
        binding.productCart.setOnClickListener {
            lifecycleScope.launch {
                customProgress.showLoading(
                    requireActivity(),
                    "Adding ${args.shop.title} to Cart...."
                )
                delay(5000)
                customProgress.hideLoading()
                val action = ProductDetailFragmentDirections.actionGlobalMyDialog(
                    title = "Cart!!",
                    message = "${args.shop.title}\nPrice :- ${ExtraFile.Rs} ${args.shop.price}"
                )
                findNavController().navigate(action)
            }
        }

        binding.productBuy.setOnClickListener {
            lifecycleScope.launch {
                customProgress.showLoading(requireActivity(), "Purchasing ${args.shop.title}..")
                delay(5000)
                customProgress.hideLoading()
                val action = ProductDetailFragmentDirections.actionGlobalMyDialog(
                    title = "Item Purchased !!",
                    message = "${args.shop.title}\nPrice :- ${ExtraFile.Rs} ${args.shop.price}\nMoney Saved :- ${ExtraFile.Rs} ${(args.maxprice.toDouble() - args.shop.price).toInt()}"
                )
                findNavController().navigate(action)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        customProgress.hideLoading()
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