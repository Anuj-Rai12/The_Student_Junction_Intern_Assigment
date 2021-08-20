package com.example.cargo.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
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
    private var myLogoutDialog: MyLogoutDialog? = null

    @Inject
    lateinit var customProgress: CustomProgress

    @Inject
    lateinit var pagingAdapter: PagingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding = ProductScreenFragmentBinding.bind(view)
        savedInstanceState?.let {
            dialogFlag = it.getBoolean(ExtraFile.show_dialog_once)
        }
        if (dialogFlag == true)
            openDialog()

        showLoading()
        showRecycle()
        setData()
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
            }
        }
    }

    private fun showRecycle() {
        binding.apply {
            MainRecycleView.apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(requireContext())
                adapter = pagingAdapter
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.logout_menu, menu)
        val log = menu.findItem(R.id.logout_btn)
        log.setOnMenuItemClickListener {
            openDialog()
            return@setOnMenuItemClickListener true
        }
    }

    private fun openDialog() {
        myLogoutDialog = MyLogoutDialog(
            title = ExtraFile.log_out_msg,
            msg = "Are you Sure Want to LogOut?"
        )
        myLogoutDialog?.show(childFragmentManager, ExtraFile.log_out_msg)
        dialogFlag = true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        dialogFlag?.let {
            outState.putBoolean(ExtraFile.show_dialog_once, it)
        }
    }
}