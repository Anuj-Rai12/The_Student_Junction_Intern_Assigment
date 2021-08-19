package com.example.data

sealed class DataSealed {
    data class UserDescription(
        val title: String,
        val description: String
    ) : DataSealed()

    data class Category(
        val title: String,
        val textBtn: String
    ) : DataSealed()

    data class ShoppingInfo(
        val shoppingProductItem: ShoppingProductItem
    ) : DataSealed()
}
