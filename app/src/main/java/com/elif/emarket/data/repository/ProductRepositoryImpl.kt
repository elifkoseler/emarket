package com.elif.emarket.data.repository

import com.elif.emarket.domain.repository.ProductRepository
import com.elif.emarket.data.model.toDomain
import com.elif.emarket.data.remote.ProductApiService
import com.elif.emarket.domain.model.Product

class ProductRepositoryImpl(
    private val apiService: ProductApiService
) : ProductRepository {

    override suspend fun getProducts(): List<Product> {
        return apiService.getProducts().map { it.toDomain() }
    }
}

