package com.example.getapiinfo

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path





interface FakeStoreApi {
    @GET("products")
    suspend fun getAllProducts(): Response<List<Product>>

    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Response<Product>
}
