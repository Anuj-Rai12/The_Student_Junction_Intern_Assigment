package com.example.cargo.data

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


sealed class DataProduction {
    data class ImageUri(
        val uri: String
    ) : DataProduction()

    data class ShoppingInfo(
        val shoppingProductItem: ShoppingProductItem,
        val maxPrice: Double? = null
    ) : DataProduction()

    data class BtnLayout(
        val btn1: String,
        val btn2: String
    ) : DataProduction()
}
