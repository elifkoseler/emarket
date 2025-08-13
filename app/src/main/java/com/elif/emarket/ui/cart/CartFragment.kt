package com.elif.emarket.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.elif.emarket.databinding.FragmentCartBinding
import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.ui.cart.adapter.CartAdapter

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val cartItems = CartManager.getItems().toMutableList()

        cartAdapter = CartAdapter(cartItems) {
            updateTotalPrice(cartItems)
        }

        binding.rvCartItems.adapter = cartAdapter
        updateTotalPrice(cartItems)

        binding.btnComplete.setOnClickListener {
            Toast.makeText(requireContext(), "Order Completed!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateTotalPrice(cartItems: List<CartItem>) {
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
        _binding = null
    }
}
