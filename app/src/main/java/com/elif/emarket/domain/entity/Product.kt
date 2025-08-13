package com.elif.emarket.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int? = null,
    val name: String,
    val description: String = "",
    val brand: String = "",
    val price: Double = 0.0,
    val imageUrl: String = "",
    val isFavorite: Boolean = false,
    val quantity: Int = 0
) : Parcelable