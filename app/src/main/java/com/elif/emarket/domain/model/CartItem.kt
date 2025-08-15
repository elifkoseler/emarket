package com.elif.emarket.domain.model

data class CartItem(
    val productName: String,
    val price: Int,
    var quantity: Int
)
