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
import com.elif.emarket.ui.filter.FilterData
import com.elif.emarket.ui.filter.SortOption

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getProductsUseCase = GetProductsUseCase(
        ProductRepositoryImpl(RetrofitClient.api)
    )

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products = _products.asStateFlow()

    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())

    private val _filterData = MutableStateFlow(FilterData())
    val filterData = _filterData.asStateFlow()

    private val _showFilter = MutableStateFlow(false)
    val showFilter = _showFilter.asStateFlow()

    private val _availableBrands = MutableStateFlow<List<String>>(emptyList())
    val availableBrands = _availableBrands.asStateFlow()

    private val _availableModels = MutableStateFlow<List<String>>(emptyList())
    val availableModels = _availableModels.asStateFlow()

    fun loadProducts() {
        viewModelScope.launch {
            val result = getProductsUseCase()
            _allProducts.value = result
            updateAvailableOptions()
            applyFilters()
        }
    }

    private fun updateAvailableOptions() {
        val products = _allProducts.value

        _availableBrands.value = products
            .mapNotNull { it.brand?.takeIf { brand -> brand.isNotBlank() } }
            .distinct()
            .sorted()

        _availableModels.value = products
            .mapNotNull { it.model?.takeIf { model -> model.isNotBlank() } }
            .distinct()
            .sorted()
    }

    fun showFilterScreen() {
        _showFilter.value = true
    }

    fun hideFilterScreen() {
        _showFilter.value = false
    }

    fun updateFilter(newFilter: FilterData) {
        _filterData.value = newFilter
        applyFilters()
    }

    private fun applyFilters() {
        var filteredProducts = _allProducts.value
        val filter = _filterData.value

        // Brand filter
        if (filter.selectedBrands.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                product.brand?.let { brand ->
                    filter.selectedBrands.contains(brand)
                } ?: false
            }
        }

        // Model filter
        if (filter.selectedModels.isNotEmpty()) {
            filteredProducts = filteredProducts.filter { product ->
                product.model?.let { model ->
                    filter.selectedModels.contains(model)
                } ?: false
            }
        }

        filteredProducts = when (filter.sortBy) {
            SortOption.OLD_TO_NEW -> filteredProducts.sortedBy {
                it.createdAt
            }
            SortOption.NEW_TO_OLD -> filteredProducts.sortedByDescending {
                it.createdAt
            }
            SortOption.PRICE_HIGH_TO_LOW -> filteredProducts.sortedByDescending {
                it.price
            }
            SortOption.PRICE_LOW_TO_HIGH -> filteredProducts.sortedBy {
                it.price
            }
        }

        _products.value = filteredProducts
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