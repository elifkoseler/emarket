package com.elif.emarket.ui.cart.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elif.emarket.databinding.ItemCartProductBinding
import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.ui.cart.CartManager

class CartAdapter(
    private var cartItems: MutableList<CartItem>,
    private val onCartUpdated: () -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartProductBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        holder.binding.apply {
            tvProductName.text = item.productName
            tvProductPrice.text = "${item.price} ₺"
            tvQuantity.text = item.quantity.toString()

            btnIncrease.setOnClickListener {
                val newQuantity = item.quantity + 1
                CartManager.updateItemQuantity(item.productName, newQuantity)
                updateCart()
            }

            btnDecrease.setOnClickListener {
                val newQuantity = item.quantity - 1
                CartManager.updateItemQuantity(item.productName, newQuantity)
                updateCart()
            }
        }
    }

    override fun getItemCount(): Int = cartItems.size

    @SuppressLint("NotifyDataSetChanged")
    private fun updateCart() {
        // Refresh list from CartManager
        cartItems = CartManager.getItems().toMutableList()
        notifyDataSetChanged()
        onCartUpdated()
    }
}
