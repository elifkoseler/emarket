package com.elif.emarket.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elif.emarket.data.remote.RetrofitClient
import com.elif.emarket.data.repository.ProductRepositoryImpl
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.domain.usecase.GetProductsUseCase

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
}
