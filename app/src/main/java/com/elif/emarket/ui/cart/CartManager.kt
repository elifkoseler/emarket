package com.elif.emarket.ui.cart

import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.domain.entity.Product

object CartManager {
    private val cartItems = mutableListOf<CartItem>()

    fun addItem(product: Product) {
        val existing = cartItems.find { it.productName == product.name }
        if (existing != null) {
            existing.quantity++
        } else {
            cartItems.add(CartItem(product.name, product.price.toInt(), 1))
        }
    }

    fun getItems(): List<CartItem> = cartItems

    fun setItems(items: List<CartItem>) {
        cartItems.clear()
        cartItems.addAll(items)
    }

    fun clear() = cartItems.clear()

    fun updateItemQuantity(productName: String, newQuantity: Int) {
        val item = cartItems.find { it.productName == productName }
        if (item != null) {
            if (newQuantity <= 0) {
                cartItems.remove(item) // 0 olursa çıkar
            } else {
                item.quantity = newQuantity
            }
        }
    }

}
