package com.elif.emarket.domain.usecase

import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.domain.repository.CartRepository

class InsertProductToLocalUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(product: Product) {
        return repository.insertProduct(
            CartItem(
                productName = product.name,
                price = product.price.toInt(),
                quantity = 1 // Default quantity for new items
            )
        )
    }
}