package com.elif.emarket.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.elif.emarket.domain.model.CartItem

@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey
    val productName: String,
    val price: Int,
    val quantity: Int
)

// CartItem → CartItemEntity (Rooma kaydetmek için)
fun CartItem.toEntity(): CartItemEntity {
    return CartItemEntity(
        productName = this.productName,
        price = this.price,
        quantity = this.quantity
    )
}

// CartItemEntity → CartItem (Roomdan okumak için)
fun CartItemEntity.toDomain(): CartItem {
    return CartItem(
        productName = this.productName,
        price = this.price,
        quantity = this.quantity
    )
}

fun List<CartItem>.toEntityList(): List<CartItemEntity> {
    return this.map { it.toEntity() }
}

fun List<CartItemEntity>.toDomainList(): List<CartItem> {
    return this.map { it.toDomain() }
}