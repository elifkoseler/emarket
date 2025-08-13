package com.elif.emarket.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.domain.repository.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val repository: CartRepository) : ViewModel() {

    val cartItems: LiveData<List<CartItem>> = repository.getCartProducts().asLiveData()

    fun insertAll(itemList: List<CartItem>) = viewModelScope.launch {
        repository.insertAll(itemList)
    }

    fun updateItem(item: CartItem) = viewModelScope.launch {
        repository.updateProduct(item)
    }

    fun deleteItem(item: CartItem) = viewModelScope.launch {
        repository.deleteProduct(item)
    }

    fun clearCart() = viewModelScope.launch {
        repository.clearCart()
    }

    fun saveCartToRoom(items: List<CartItem>) {
        viewModelScope.launch {
            repository.clearCart()
            repository.insertAll(items)
        }
    }

    fun loadCartFromRoom(): LiveData<List<CartItem>> {
        return repository.getCartProducts().asLiveData()
    }
}

