package com.elif.emarket.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.elif.emarket.databinding.FragmentHomeBinding
import com.elif.emarket.ui.cart.CartManager
import com.elif.emarket.ui.filter.FilterScreen
import com.elif.emarket.ui.home.adapter.ProductAdapter
import kotlinx.coroutines.launch

open class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = ProductAdapter(onProductClick = { selectedProduct ->
        val action = HomeFragmentDirections.actionHomeFragmentToProductFragment(selectedProduct)
        findNavController().navigate(action)
    }, onAddToCartClick = { selectedProduct ->
        CartManager.addItem(selectedProduct)
        viewModel.saveProductToLocal(selectedProduct, requireContext())
    }, onFavoriteClick = {})

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Filter butonuna tıklama listener'ı ekle
        binding.btnSelectFilter.setOnClickListener {
            viewModel.showFilterScreen()
        }

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

        val composeView = ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            visibility = View.GONE
            setContent {
                val showFilter by viewModel.showFilter.collectAsState()
                val filterData by viewModel.filterData.collectAsState()
                val availableBrands by viewModel.availableBrands.collectAsState()
                val availableModels by viewModel.availableModels.collectAsState()

                LaunchedEffect(showFilter) {
                    visibility = if (showFilter) View.VISIBLE else View.GONE
                }
                if (showFilter) binding.searchCard.visibility = View.GONE
                else binding.searchCard.visibility = View.VISIBLE


                if (showFilter) {
                    FilterScreen(
                        initialFilter = filterData,
                        availableBrands = availableBrands,
                        availableModels = availableModels,
                        onFilterChanged = { newFilter ->
                            viewModel.updateFilter(newFilter)
                        },
                        onDismiss = {
                            viewModel.hideFilterScreen()
                        })
                }
            }
        }

        val rootLayout = binding.root as ViewGroup
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        composeView.layoutParams = layoutParams
        rootLayout.addView(composeView)

        viewModel.loadProducts()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}