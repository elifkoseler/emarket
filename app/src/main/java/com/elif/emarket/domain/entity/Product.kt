package com.elif.emarket.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import retrofit2.http.Query

@Parcelize
data class Product(
    val id: Int,
    val name: String,
    val description: String = "",
    val brand: String = "",
    val price: Double,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val quantity: Int = 0
) : Parcelable