package com.elif.emarket.domain.entity

data class CartItem(
    val productName: String,
    val price: Int,
    var quantity: Int
)
