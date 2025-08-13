package com.elif.emarket.domain.usecase

import com.elif.emarket.domain.repository.ProductRepository
import com.elif.emarket.domain.entity.Product

class GetProductsUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(): List<Product> {
        return repository.getProducts()
    }
}
