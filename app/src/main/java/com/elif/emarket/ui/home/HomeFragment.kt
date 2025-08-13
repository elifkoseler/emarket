package com.elif.emarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.elif.emarket.databinding.FragmentHomeBinding
import com.elif.emarket.ui.cart.CartManager
import com.elif.emarket.ui.home.adapter.ProductAdapter
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ProductAdapter(
        onProductClick = { selectedProduct ->
            val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(selectedProduct)
            findNavController().navigate(action)
        },
        onAddToCartClick = { selectedProduct ->
            CartManager.addItem(selectedProduct)
            viewModel.saveProductToLocal(selectedProduct, requireContext())
        },
        onFavoriteClick = {}
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter

        lifecycleScope.launch {
            viewModel.products.collect { list ->
                adapter.submitList(list)
            }
        }

        viewModel.loadProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
