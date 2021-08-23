package com.example.cargo.paginate.recycle

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.cargo.R
import com.example.cargo.databinding.CategoryItemBinding
import com.example.cargo.databinding.LayoutBtnClickBinding
import com.example.cargo.databinding.UserDescriptionBinding
import com.example.cargo.paginate.AllViewHolder
import com.example.cargo.utils.TAG
import com.example.cargo.data.DataProduction


class ProductAdaptor constructor(private val function: (String) -> Unit) :
    ListAdapter<DataProduction, AllViewHolder>(diffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        return when (viewType) {
            R.layout.user_description -> {
                AllViewHolder.TitleViewHolder(
                    binding = UserDescriptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.category_item -> {
                AllViewHolder.CategoryViewHolder(
                    binding = CategoryItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            R.layout.layout_btn_click -> {
                AllViewHolder.ButtonLayout(
                    binding = LayoutBtnClickBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                throw IllegalArgumentException("Invalid View Type")
            }
        }
    }

    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
        val curr = getItem(position)
        curr?.let { dataProduction ->
            when (holder) {
                is AllViewHolder.CategoryViewHolder -> holder.showItemProduct(dataProduction as DataProduction.ImageUri)
                is AllViewHolder.ShoppingViewHolder -> Log.i(
                    TAG,
                    "onBindViewHolder: Not here Shopping"
                )
                is AllViewHolder.TitleViewHolder -> holder.showProductDetail(dataProduction as DataProduction.ShoppingInfo)
                is AllViewHolder.ButtonLayout -> holder.bindIt(
                    dataProduction as DataProduction.BtnLayout,
                    function = function
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataProduction.ImageUri -> R.layout.category_item
            is DataProduction.ShoppingInfo -> R.layout.user_description
            is DataProduction.BtnLayout -> R.layout.layout_btn_click
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DataProduction>() {
            override fun areItemsTheSame(
                oldItem: DataProduction,
                newItem: DataProduction
            ): Boolean {
                return getValue(oldItem) == getValue(newItem)
            }

            override fun areContentsTheSame(
                oldItem: DataProduction,
                newItem: DataProduction
            ): Boolean {
                return oldItem == newItem
            }
        }

        private fun getValue(newItem: DataProduction): String {
            return when (newItem) {
                is DataProduction.ImageUri -> newItem.uri
                is DataProduction.ShoppingInfo -> newItem.shoppingProductItem.title
                is DataProduction.BtnLayout -> newItem.btn2
            }
        }
    }
}