package com.elif.emarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.elif.emarket.databinding.FragmentHomeBinding
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.ui.home.adapter.ProductAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var productsAdapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        loadDummyProducts() // test için
    }

    private fun setupUI() {
        productsAdapter = ProductAdapter(onProductClick = { product ->
            // TODO: Ürün detay sayfasına git
        }, onAddToCartClick = { product ->
            // TODO: Sepete ekle işlemi
        }, onFavoriteClick = { product ->
            // TODO: Favoriye ekle/çıkar
        })

        binding.rvProducts.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = productsAdapter
        }
    }

    private fun loadDummyProducts() {
        val products = listOf(
            Product(1, "Ayakkabı", price = 199.99, imageUrl = "https://example.com/ayakkabi.jpg", isFavorite =true),
            Product(2, "Tişört", price = 79.99, imageUrl ="https://example.com/tisort.jpg", isFavorite =false),
            Product(3, "Pantolon", price = 149.99, imageUrl ="https://example.com/pantolon.jpg", isFavorite =true),
            Product(4, "Ceket", price = 299.99, imageUrl ="https://example.com/ceket.jpg", isFavorite =true),
            Product(2, "Tişört", price = 79.99, imageUrl ="https://example.com/tisort.jpg", isFavorite =false),
            Product(3, "Pantolon", price = 149.99, imageUrl ="https://example.com/pantolon.jpg", isFavorite =true),
            Product(4, "Ceket", price = 299.99, imageUrl ="https://example.com/ceket.jpg", isFavorite =  true)
        )
        productsAdapter.submitList(products)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
