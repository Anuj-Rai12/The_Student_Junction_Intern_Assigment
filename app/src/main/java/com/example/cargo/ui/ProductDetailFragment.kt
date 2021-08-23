package com.example.cargo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.example.cargo.data.DataProduction
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProductDetailFragment : Fragment(R.layout.product_description_fragment) {
    private lateinit var binding: ProductDescriptionFragmentBinding
    private val list: MutableList<DataProduction> = mutableListOf()
    private val args: ProductDetailFragmentArgs by navArgs()
    private lateinit var productAdaptor: ProductAdaptor

    @Inject
    lateinit var customProgress: CustomProgress

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = ProductDescriptionFragmentBinding.bind(view)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        Log.i(TAG, "onViewCreated: ${args.shop}\n${args.maxprice}")
        binding.categoryTitle.text = args.category
        setUpRecycle()
        setData()
        binding.arrowImg.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onPause() {
        super.onPause()
        customProgress.hideLoading()
    }

    private suspend fun onBtnClick(choose: Int = 0, title: String) {
        customProgress.showLoading(requireActivity(), title)
        delay(5000)
        customProgress.hideLoading()
        val action = when (choose) {
            0 -> {
                ProductDetailFragmentDirections.actionGlobalMyDialog(
                    title = "Cart!!",
                    message = "${args.shop.title}\nPrice :- ${ExtraFile.Rs} ${args.shop.price}"
                )
            }
            else -> {
                ProductDetailFragmentDirections.actionGlobalMyDialog(
                    title = "Item Purchased !!",
                    message = "${args.shop.title}\nPrice :- ${ExtraFile.Rs} ${args.shop.price}\nMoney Saved :- ${ExtraFile.Rs} ${(args.maxprice.toDouble() - args.shop.price).toInt()}"
                )
            }
        }
        findNavController().navigate(action)
    }

    private fun setUpRecycle() {
        binding.apply {
            productLayout.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                productAdaptor = ProductAdaptor { btn ->
                    itemClick(btn)
                }
                adapter = productAdaptor
            }
        }
    }

    private fun itemClick(btn: String) {
        lifecycleScope.launch {
            when (btn) {
                getString(R.string.app_cart) -> {
                    onBtnClick(title = "Adding ${args.shop.title} to Cart....")
                }
                else -> {
                    onBtnClick(23, title = "Purchasing ${args.shop.title}..")
                }
            }
        }
    }

    private fun setData() {
        list.add(DataProduction.ImageUri(args.shop.image))
        list.add(DataProduction.ShoppingInfo(args.shop, args.maxprice.toDouble()))
        list.add(
            DataProduction.BtnLayout(
                btn1 = getString(R.string.app_buy),
                btn2 = getString(R.string.app_cart)
            )
        )
        productAdaptor.submitList(list)
    }
}