package com.elif.emarket.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String = "",
    val brand: String = "",
    val price: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false
) : Parcelable