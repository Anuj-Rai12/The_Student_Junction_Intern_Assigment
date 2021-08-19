package com.example.cargo.di

import com.example.cargo.api.ShoppingApi
import com.example.cargo.utils.ExtraFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object Module {

    private val client by lazy {
        OkHttpClient.Builder().apply {
            readTimeout(20, TimeUnit.SECONDS)
            writeTimeout(20, TimeUnit.SECONDS)
            connectTimeout(30, TimeUnit.SECONDS)
        }.build()
    }

    @Provides
    @Singleton
    fun getRetrofit(): ShoppingApi =
        Retrofit.Builder()
            .baseUrl(ExtraFile.Base_Url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShoppingApi::class.java)
}