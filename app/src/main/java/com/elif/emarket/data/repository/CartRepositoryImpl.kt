package com.elif.emarket.data.repository

import com.elif.emarket.data.local.dao.CartDao
import com.elif.emarket.data.local.entity.toDomain
import com.elif.emarket.data.local.entity.toEntity
import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CartRepositoryImpl(
    private val cartDao: CartDao
) : CartRepository {

    override fun getCartProducts(): Flow<List<CartItem>> = cartDao.getCartItems()
        .map { entities -> entities.map { it.toDomain() } }

    override suspend fun insertAll(productList: List<CartItem>) = cartDao.insertAll(productList.map { it.toEntity() })

    override suspend fun insertProduct(product: CartItem) = cartDao.insertCartItem(product.toEntity())

    override suspend fun updateProduct(product: CartItem) = cartDao.updateCartItem(product.toEntity())

    override suspend fun deleteProduct(product: CartItem) = cartDao.deleteCartItem(product.toEntity())

    override suspend fun clearCart() = cartDao.clearCart()
}
