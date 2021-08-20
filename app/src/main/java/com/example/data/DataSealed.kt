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


sealed class DataProduction {
    data class ImageUri(
        val uri:String
    ) : DataProduction()

    data class ShoppingInfo(
        val shoppingProductItem: ShoppingProductItem,
        val maxPrice:Double?=null
    ) : DataProduction()
}
