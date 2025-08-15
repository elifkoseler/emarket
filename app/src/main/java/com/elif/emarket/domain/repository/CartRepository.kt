package com.elif.emarket.domain.repository

import com.elif.emarket.domain.model.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartProducts(): Flow<List<CartItem>>
    suspend fun insertAll(productList: List<CartItem>)
    suspend fun insertProduct(product: CartItem)
    suspend fun updateProduct(product: CartItem)
    suspend fun deleteProduct(product: CartItem)
    suspend fun clearCart()
}
