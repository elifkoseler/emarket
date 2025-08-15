package com.elif.emarket.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elif.emarket.data.local.AppDatabase
import com.elif.emarket.data.repository.CartRepositoryImpl
import com.elif.emarket.databinding.FragmentCartBinding
import com.elif.emarket.domain.model.CartItem
import com.elif.emarket.ui.cart.adapter.CartAdapter

class CartFragment : Fragment() {
    lateinit var binding: FragmentCartBinding
    private lateinit var cartAdapter: CartAdapter
    private lateinit var viewModel: CartViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        val dao = AppDatabase.getInstance(requireContext()).cartDao()
        val repository = CartRepositoryImpl(dao)
        viewModel = CartViewModel(repository)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        cartAdapter = CartAdapter(mutableListOf()) {
            updateTotalPrice(CartManager.getItems())
        }
        binding.rvCartItems.adapter = cartAdapter

        viewModel.cartItems.observe(viewLifecycleOwner) { savedItems ->
            CartManager.setItems(savedItems)
            cartAdapter.updateList(savedItems.toMutableList())
            updateTotalPrice(savedItems)
        }

        binding.btnComplete.setOnClickListener {
            Toast.makeText(requireContext(), "Order Completed!", Toast.LENGTH_SHORT).show()
            CartManager.clear()
            viewModel.clearCart()
            cartAdapter.updateList(mutableListOf())
            updateTotalPrice(mutableListOf())
        }
    }

    fun updateTotalPrice(cartItems: List<CartItem>) {
        binding.tvEmptyCartMessage.visibility = if (cartItems.isEmpty()) View.VISIBLE else View.GONE
        binding.rvCartItems.visibility = if (cartItems.isEmpty()) View.GONE else View.VISIBLE

        if (cartItems.isEmpty()) {
            binding.tvTotalPrice.text = "0 ₺"
            return
        } else {
            val total = CartManager.getItems().sumOf { it.price * it.quantity }
            binding.tvTotalPrice.text = "${total} ₺"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.saveCartToRoom(CartManager.getItems())
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadCartFromRoom().observe(viewLifecycleOwner) { savedItems ->
            if (savedItems.isNotEmpty()) {
                CartManager.setItems(savedItems)
                cartAdapter = CartAdapter(savedItems.toMutableList()) {
                    updateTotalPrice(savedItems)
                }
                binding.rvCartItems.adapter = cartAdapter
                updateTotalPrice(savedItems)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.saveCartToRoom(CartManager.getItems())
    }
}
