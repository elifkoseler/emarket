package com.elif.emarket.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.elif.emarket.R
import com.elif.emarket.databinding.ItemProductBinding
import com.elif.emarket.domain.entity.Product

class ProductAdapter(
    val onProductClick: (Product) -> Unit,
    val onAddToCartClick: (Product) -> Unit,
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

        fun bind(product: Product) = with(binding) {
            tvProductName.text = product.name
            tvPrice.text = "${product.price.toInt()} ₺"

            ivProduct.load(product.imageUrl) {
                crossfade(true)
            }

            ivFavorite.setImageResource(
                if (product.isFavorite) R.drawable.star_checked
                else R.drawable.star_unchecked
            )

            root.setOnClickListener { onProductClick(product) }
            btnAddToCart.setOnClickListener { onAddToCartClick(product) }

            ivFavorite.setOnClickListener {
                val updated = product.copy(isFavorite = !product.isFavorite)

                val newList = this@ProductAdapter.currentList.toMutableList()
                val idx = newList.indexOfFirst { it.id == updated.id }
                if (idx != -1) {
                    newList[idx] = updated
                    this@ProductAdapter.submitList(newList)
                }

                onFavoriteClick(updated)
            }
        }
    }

    private class ProductDiffCallback : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean =
            oldItem == newItem
    }
}
