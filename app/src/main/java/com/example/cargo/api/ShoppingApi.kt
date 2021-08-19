package com.example.cargo.api

import com.example.cargo.utils.ExtraFile
import com.example.data.ShoppingProduct
import retrofit2.http.GET
import retrofit2.http.Query

interface ShoppingApi {

    @GET(ExtraFile.param_key)
    suspend fun getShoppingApi(
        @Query("limit") limit: Int
    ):ShoppingProduct
}