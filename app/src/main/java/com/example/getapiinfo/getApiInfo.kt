package com.example.getapiinfo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitObj {
    val api: FakeStoreApi by lazy {
        Retrofit.Builder().baseUrl("https://fakestoreapi.com/")
            .addConverterFactory(GsonConverterFactory.create()).build()
            .create(FakeStoreApi::class.java)
    }
}