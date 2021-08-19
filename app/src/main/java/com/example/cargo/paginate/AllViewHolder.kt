package com.example.cargo.paginate

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.example.cargo.databinding.CategoryItemBinding
import com.example.cargo.databinding.ProductItemBinding
import com.example.cargo.databinding.UserDescriptionBinding
import com.example.cargo.utils.ExtraFile
import com.example.data.DataSealed

sealed class AllViewHolder(binding:ViewBinding) :RecyclerView.ViewHolder(binding.root){
    class TitleViewHolder(private val binding: UserDescriptionBinding) : AllViewHolder(binding) {
        fun bindIt(userInfo: DataSealed.UserDescription) {
            binding.apply {
                userTitle.text = userInfo.title
                usersDescription.text = userInfo.description
            }
        }
    }

    class CategoryViewHolder(private val binding: CategoryItemBinding) : AllViewHolder(binding) {
        fun bindIt(category: DataSealed.Category) {
            binding.apply {
                seeAll.text = category.textBtn
                myCategories.text = category.title
            }
        }
    }

    class  ShoppingViewHolder(private val binding: ProductItemBinding):AllViewHolder(binding){
        @SuppressLint("SetTextI18n")
        fun bindIt(shop:DataSealed.ShoppingInfo){
            binding.apply {
                MRPPrice.text="${ExtraFile.Rs} 1000"
                MRPPrice.paint.isStrikeThruText=true
                currentPrice.text="${ExtraFile.Rs} ${shop.shoppingProductItem.price}"
                productCategory.text="# ${shop.shoppingProductItem.category}"
                productDescription.text=shop.shoppingProductItem.description
                productImage.load(shop.shoppingProductItem.image)
                productTitle.text=shop.shoppingProductItem.title
                discountPrice.text="10%"
            }
        }
    }
}