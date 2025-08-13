package com.elif.emarket.domain.repository

import com.elif.emarket.domain.entity.Product

interface ProductRepository {
    suspend fun getProducts(): List<Product>
}