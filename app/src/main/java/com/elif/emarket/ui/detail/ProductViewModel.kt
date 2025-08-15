package com.elif.emarket.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elif.emarket.data.local.AppDatabase
import com.elif.emarket.data.repository.CartRepositoryImpl
import com.elif.emarket.domain.model.Product
import com.elif.emarket.domain.usecase.InsertProductToLocalUseCase
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    fun saveProductToLocal(product: Product, context: Context) {
        val insertProductToLocalUseCase = InsertProductToLocalUseCase(
            CartRepositoryImpl(AppDatabase.getInstance(context).cartDao())
        )
        viewModelScope.launch {
            insertProductToLocalUseCase(product)
        }
    }
}