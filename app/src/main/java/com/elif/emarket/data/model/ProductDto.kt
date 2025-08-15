package com.elif.emarket.data.model

import com.elif.emarket.domain.entity.Product

data class ProductDto(
    val id: String,
    val name: String,
    val description: String,
    val brand: String,
    val price: String,
    val image: String,
    val createdAt: String,
    val model: String,
)

fun ProductDto.toDomain(): Product {
    return Product(
        id = id.toInt(),
        name = name,
        description = description,
        brand = brand,
        price = price.toDoubleOrNull() ?: 0.0,
        imageUrl = image,
        createdAt = createdAt,
        model = model
    )
}
