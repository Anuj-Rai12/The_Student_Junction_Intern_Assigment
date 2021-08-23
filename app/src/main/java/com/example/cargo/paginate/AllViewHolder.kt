package com.example.cargo.paginate

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import coil.load
import com.example.cargo.databinding.CategoryItemBinding
import com.example.cargo.databinding.LayoutBtnClickBinding
import com.example.cargo.databinding.ProductItemBinding
import com.example.cargo.databinding.UserDescriptionBinding
import com.example.cargo.utils.ExtraFile
import com.example.cargo.utils.fetchDis
import com.example.cargo.utils.hide
import com.example.cargo.utils.show
import com.example.cargo.data.DataProduction
import com.example.cargo.data.DataSealed
import com.example.cargo.data.ShoppingProductItem

sealed class AllViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
    class TitleViewHolder(private val binding: UserDescriptionBinding) : AllViewHolder(binding) {
        fun bindIt(userInfo: DataSealed.UserDescription) {
            binding.apply {
                userTitle.text = userInfo.title
                usersDescription.text = userInfo.description
            }
        }

        @SuppressLint("SetTextI18n")
        fun showProductDetail(shop: DataProduction.ShoppingInfo) {
            binding.apply {
                val currPrice = shop.shoppingProductItem.price
                val maxPrice = shop.maxPrice!!
                userTitle.text = shop.shoppingProductItem.title
                usersDescription.text = shop.shoppingProductItem.description
                ProductCurrentPrice.apply {
                    show()
                    setPadding(0, 0, 0, 20)
                    text = "${ExtraFile.Rs} $currPrice"
                }
                ProductMrpPrice.apply {
                    show()
                    setPadding(0, 0, 0, 20)
                    text = "${ExtraFile.Rs} ${maxPrice.toInt()}"
                    paint.isStrikeThruText = true
                }
                productDiscountPrice.apply {
                    show()
                    setPadding(0, 0, 0, 20)
                    text = "${fetchDis(currPrice, maxPrice)}% off"
                }
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

        fun showItemProduct(img: DataProduction.ImageUri) {
            binding.apply {
                seeAll.hide()
                myCategories.hide()
                productImageView.show()
                productImageView.load(img.uri)
            }
        }
    }

    class ShoppingViewHolder(private val binding: ProductItemBinding) : AllViewHolder(binding) {
        @SuppressLint("SetTextI18n")
        fun bindIt(
            shop: DataSealed.ShoppingInfo,
            product: (shop: ShoppingProductItem, maxPrice: String) -> Unit,
            position: Int
        ) {
            binding.apply {
                val currPrice = shop.shoppingProductItem.price
                val mrpPrice = (shop.shoppingProductItem.price) * position
                MRPPrice.text = "${ExtraFile.Rs} ${mrpPrice.toInt()}"
                MRPPrice.paint.isStrikeThruText = true
                currentPrice.text = "${ExtraFile.Rs} $currPrice"
                productCategory.text = "# ${shop.shoppingProductItem.category}"
                productDescription.text = shop.shoppingProductItem.description
                productImage.load(shop.shoppingProductItem.image)
                productTitle.text = shop.shoppingProductItem.title
                discountPrice.text = "${fetchDis(currPrice, mrpPrice)}%"
                binding.root.setOnClickListener {
                    product(shop.shoppingProductItem, mrpPrice.toString())
                }
            }
        }
    }

    class ButtonLayout(private val binding: LayoutBtnClickBinding) : AllViewHolder(binding) {
         fun bindIt(btn: DataProduction.BtnLayout, function: (String) -> Unit) {
            binding.apply {
                productBuy.text = btn.btn1
                productCart.text = btn.btn2
                binding.productBuy.setOnClickListener {
                    function(btn.btn1)
                }
                binding.productCart.setOnClickListener {
                    function(btn.btn2)
                }
            }
        }
    }
}