package com.example.cargo.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cargo.R
import com.example.cargo.databinding.ProductScreenFragmentBinding
import com.example.cargo.paginate.PagingAdapter
import com.example.cargo.utils.ExtraFile
import com.example.cargo.viewmodel.MyViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class ShoppingDashBoardFragment : Fragment(R.layout.product_screen_fragment) {
    private lateinit var binding: ProductScreenFragmentBinding
    private val viewModel: MyViewModel by viewModels()

    @Inject
    lateinit var pagingAdapter: PagingAdapter
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        binding = ProductScreenFragmentBinding.bind(view)
        showRecycle()
        setData()
        setHasOptionsMenu(true)
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
            val action = ShoppingDashBoardFragmentDirections.actionGlobalMyDialog(
                title = ExtraFile.log_out_msg,
                message = "Are you Sure you Want to Log Out?"
            )
            findNavController().navigate(action)
            return@setOnMenuItemClickListener true
        }
    }
}