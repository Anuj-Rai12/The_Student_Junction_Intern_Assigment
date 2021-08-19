package com.example.cargo.paginate

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.cargo.R
import com.example.cargo.databinding.CategoryItemBinding
import com.example.cargo.databinding.ProductItemBinding
import com.example.cargo.databinding.UserDescriptionBinding
import com.example.cargo.utils.TAG
import com.example.data.DataSealed
import javax.inject.Inject

class PagingAdapter @Inject constructor() : PagingDataAdapter<DataSealed, AllViewHolder>(diffUtil) {
    override fun onBindViewHolder(holder: AllViewHolder, position: Int) {
        val current = getItem(position)
        current?.let { dataItem ->
            when (holder) {
                //Data for Category
                is AllViewHolder.CategoryViewHolder -> holder.bindIt(category = dataItem as DataSealed.Category)
                //Data for Shopping
                is AllViewHolder.ShoppingViewHolder -> holder.bindIt(
                    shop = dataItem as DataSealed.ShoppingInfo

                )
                //Data for Title
                is AllViewHolder.TitleViewHolder -> holder.bindIt(userInfo = dataItem as DataSealed.UserDescription)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolder {
        return when (viewType) {
            R.layout.product_item -> {
                AllViewHolder.ShoppingViewHolder(
                    binding = ProductItemBinding.inflate(
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
            R.layout.user_description -> {
                AllViewHolder.TitleViewHolder(
                    binding = UserDescriptionBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
            else -> {
                Log.i(TAG, "onCreateViewHolder: Not Layout found")
                throw IllegalArgumentException("Invalid View Type")
            }
        }
    }

    override fun getItemViewType(position: Int) = when (getItem(position)) {
        is DataSealed.Category -> R.layout.category_item
        is DataSealed.ShoppingInfo -> R.layout.product_item
        is DataSealed.UserDescription -> R.layout.user_description
        else -> {
            Log.i(TAG, "getItemViewType: Position -> $position")
            0
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<DataSealed>() {
            override fun areItemsTheSame(oldItem: DataSealed, newItem: DataSealed): Boolean {
                return getValue(oldItem) == getValue(newItem)
            }

            private fun getValue(dataSealed: DataSealed) {
                when (dataSealed) {
                    is DataSealed.Category -> dataSealed.title
                    is DataSealed.ShoppingInfo -> dataSealed.shoppingProductItem.image
                    is DataSealed.UserDescription -> dataSealed.title
                }
            }

            override fun areContentsTheSame(oldItem: DataSealed, newItem: DataSealed): Boolean {
                return oldItem == newItem
            }

        }
    }

}