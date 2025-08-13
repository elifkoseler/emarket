package com.elif.emarket.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elif.emarket.data.local.AppDatabase
import com.elif.emarket.data.remote.RetrofitClient
import com.elif.emarket.data.repository.CartRepositoryImpl
import com.elif.emarket.data.repository.ProductRepositoryImpl
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.domain.usecase.GetProductsUseCase
import com.elif.emarket.domain.usecase.InsertProductToLocalUseCase

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getProductsUseCase = GetProductsUseCase(
        ProductRepositoryImpl(RetrofitClient.api)
    )

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            val result = getProductsUseCase()
            _products.value = result
        }
    }

    fun saveProductToLocal(product: Product, context: Context) {
        val insertProductToLocalUseCase = InsertProductToLocalUseCase(
            CartRepositoryImpl(AppDatabase.getInstance(context).cartDao())
        )
        viewModelScope.launch {
            insertProductToLocalUseCase(product)
        }
    }
}
