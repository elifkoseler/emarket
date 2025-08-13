package com.elif.emarket.data.remote

import com.elif.emarket.data.model.ProductDto
import retrofit2.http.GET

interface ProductApiService {
    @GET("products")
    suspend fun getProducts(): List<ProductDto>
}
