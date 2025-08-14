package com.elif.emarket.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import coil.load
import com.elif.emarket.databinding.FragmentProductBinding
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.ui.cart.CartManager
import com.elif.emarket.ui.home.HomeViewModel
import kotlin.getValue

open class ProductFragment : Fragment() {

    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private lateinit var product: Product
    private val viewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        product = requireArguments().getParcelable("product")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupUI()
        setupToolbar()
    }

    private fun setupUI() = with(binding) {
        toolbar.title = product.name
        ivProduct.load(product.imageUrl)
        tvProductName.text = product.name
        tvDescription.text = product.description
        tvPrice.text = "${product.price} ₺"

        btnAddToCart.setOnClickListener {
            CartManager.addItem(product)
            viewModel.saveProductToLocal(product, requireContext())
            Toast.makeText(requireContext(), "${product.name} added to cart!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupToolbar() = with(binding) {
        toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}