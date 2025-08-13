package com.elif.emarket.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.elif.emarket.databinding.ItemProductBinding
import com.elif.emarket.domain.entity.Product

class ProductAdapter(
    private val onProductClick: (Product) -> Unit,
    private val onAddToCartClick: (Product) -> Unit,
    private val onFavoriteClick: (Product) -> Unit
) : ListAdapter<Product, ProductAdapter.ProductViewHolder>(ProductDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(
        private val binding: ItemProductBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
            binding.apply {
                tvProductName.text = product.name
                tvPrice.text = "${product.price.toInt()} ₺"
                
                // TODO: Load product image using Glide or Coil
                // Glide.with(ivProduct).load(product.imageUrl).into(ivProduct)
                
                // Set favorite icon state
                if (product.isFavorite) {
                    ivFavorite.setImageResource(com.elif.emarket.R.drawable.star_unchecked)
                } else {
                    ivFavorite.setImageResource(com.elif.emarket.R.drawable.star_checked)
                }
                
                // Set click listeners
                root.setOnClickListener { onProductClick(product) }
                btnAddToCart.setOnClickListener { onAddToCartClick(product) }
                ivFavorite.setOnClickListener { onFavoriteClick(product) }
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }
} 