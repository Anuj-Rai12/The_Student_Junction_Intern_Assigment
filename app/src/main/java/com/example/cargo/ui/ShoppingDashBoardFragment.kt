package com.example.cargo.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargo.R
import com.example.cargo.databinding.ProductScreenFragmentBinding
import com.example.cargo.paginate.PagingAdapter
import com.example.cargo.utils.CustomProgress
import com.example.cargo.utils.ExtraFile
import com.example.cargo.utils.MyLogoutDialog
import com.example.cargo.utils.TAG
import com.example.cargo.viewmodel.MyViewModel
import com.example.cargo.data.ShoppingProductItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ShoppingDashBoardFragment : Fragment(R.layout.product_screen_fragment) {
    private lateinit var binding: ProductScreenFragmentBinding
    private val viewModel: MyViewModel by viewModels()
    private var dialogFlag: Boolean? = null
    private var loadOnce: Boolean? = null
    private var myLogoutDialog: MyLogoutDialog? = null

    @Inject
    lateinit var customProgress: CustomProgress

    private lateinit var pagingAdapter: PagingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        binding = ProductScreenFragmentBinding.bind(view)
        savedInstanceState?.let {
            dialogFlag = it.getBoolean(ExtraFile.show_dialog_once)
            loadOnce = it.getBoolean(ExtraFile.param_key)
        }
        binding.toolbarLayoutAll.apply {
            inflateMenu(R.menu.logout_menu)
            setOnMenuItemClickListener {
                when(it.itemId){
                    R.id.logout_btn-> openDialog()
                }
                return@setOnMenuItemClickListener true
            }
        }
        if (dialogFlag == true)
            openDialog()
        showRecycle()
        setData()
        if (loadOnce == null)
            setUpUi()
        setHasOptionsMenu(true)
    }

    private fun showLoading() =
        customProgress.showLoading(requireActivity(), "Product Is Loading..")

    private fun hideLoading() = customProgress.hideLoading()
    private fun setUpUi() {
        lifecycleScope.launch {
            pagingAdapter.loadStateFlow.collectLatest {
                when (it.append) {
                    is LoadState.NotLoading -> {
                        hideLoading()
                        Log.i(TAG, "setUpUi: Not Loading")
                    }
                    LoadState.Loading -> {
                        showLoading()
                        Log.i(TAG, "setUpUi: Loading...")
                    }
                    is LoadState.Error -> {
                        Log.i(TAG, "setUpUi: Error.. ")
                        hideLoading()
                        val error = (it.append as LoadState.Error).error.localizedMessage
                        if (error.equals("List is Empty", true)) {
                            val action = ShoppingDashBoardFragmentDirections.actionGlobalMyDialog(
                                "Error",
                                error!!
                            )
                            findNavController().navigate(action)
                        }
                        Log.i(TAG, "setUpUi: Error -> $error")
                    }
                }
            }
        }
    }

    override fun onPause() {
        super.onPause()
        hideLoading()
        myLogoutDialog?.dismiss()
    }

    private fun setData() {
        lifecycleScope.launch {
            viewModel.flow.collect {
                pagingAdapter.submitData(it)
                hideLoading()
            }
        }
    }

    private fun showRecycle() {
        binding.apply {
            MainRecycleView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                pagingAdapter = PagingAdapter { shop, maxPrice ->
                    loadOnce = true
                    itemClick(shop, maxPrice)
                }
                adapter = pagingAdapter
            }
        }
    }

    private fun itemClick(shop: ShoppingProductItem, maxPrice: String) {
        val action =
            ShoppingDashBoardFragmentDirections.actionShoppingDashBoardFragmentToProductDetailFragment(
                category = "# ${shop.category}",
                shop = shop,
                maxprice = maxPrice
            )
        findNavController().navigate(action)
    }
    private fun openDialog() {
        myLogoutDialog = MyLogoutDialog(
            title = ExtraFile.log_out_msg,
            msg = "Are you Sure Want to LogOut?"
        ) { flag ->
            dialogFlag = flag
        }
        myLogoutDialog?.isCancelable = false
        myLogoutDialog?.show(childFragmentManager, ExtraFile.log_out_msg)
        dialogFlag = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dialogFlag?.let {
            outState.putBoolean(ExtraFile.show_dialog_once, it)
        }
        loadOnce?.let {
            outState.putBoolean(ExtraFile.param_key, it)
        }
    }
}