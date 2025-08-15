package com.elif.emarket.ui.cart

import android.view.View
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elif.emarket.domain.model.CartItem
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.just
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
class CartFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var scenario: FragmentScenario<CartFragment>
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        mockkObject(CartManager)
        every { CartManager.getItems() } returns emptyList()
        every { CartManager.setItems(any()) } just Runs
        every { CartManager.clear() } just Runs
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        if (::scenario.isInitialized) {
            scenario.close()
        }
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `fragment should initialize correctly`() {
        every { CartManager.getItems() } returns emptyList()

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            assert(fragment.isAdded)
            assert(fragment.view != null)
        }
    }

    @Test
    fun `empty cart should show empty message and hide recycler view`() {
        every { CartManager.getItems() } returns emptyList()

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.updateTotalPrice(emptyList())

            val binding = fragment.binding
            assert(binding.tvEmptyCartMessage.visibility == View.VISIBLE)
            assert(binding.rvCartItems.visibility == View.GONE)
            assert(binding.tvTotalPrice.text == "0 ₺")
        }
    }

    @Test
    fun `cart with items should hide empty message and show items with correct total`() {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 2),
            CartItem(productName = "Product 2", price = 20, quantity = 1)
        )
        every { CartManager.getItems() } returns cartItems

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.updateTotalPrice(cartItems)

            val binding = fragment.binding
            assert(binding.tvEmptyCartMessage.visibility == View.GONE)
            assert(binding.rvCartItems.visibility == View.VISIBLE)
            assert(binding.tvTotalPrice.text == "40 ₺")
        }
    }

    @Test
    fun `complete button click should clear cart`() {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 1)
        )
        every { CartManager.getItems() } returns cartItems

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.binding.btnComplete.performClick()
        }

        verify { CartManager.clear() }
    }

    @Test
    fun `updateTotalPrice should calculate correct total for multiple items`() {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 15, quantity = 2),
            CartItem(productName = "Product 2", price = 25, quantity = 3)
        )
        every { CartManager.getItems() } returns cartItems

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.updateTotalPrice(cartItems)

            assert(fragment.binding.tvTotalPrice.text == "105 ₺")
            assert(fragment.binding.tvEmptyCartMessage.visibility == View.GONE)
            assert(fragment.binding.rvCartItems.visibility == View.VISIBLE)
        }
    }

    @Test
    fun `updateTotalPrice with empty list should show empty state`() {
        every { CartManager.getItems() } returns emptyList()

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.updateTotalPrice(emptyList())

            assert(fragment.binding.tvTotalPrice.text == "0 ₺")
            assert(fragment.binding.tvEmptyCartMessage.visibility == View.VISIBLE)
            assert(fragment.binding.rvCartItems.visibility == View.GONE)
        }
    }

    @Test
    fun `updateTotalPrice should handle single item correctly`() {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 50, quantity = 1)
        )
        every { CartManager.getItems() } returns cartItems

        scenario = launchFragmentInContainer<CartFragment>()

        scenario.onFragment { fragment ->
            fragment.updateTotalPrice(cartItems)

            assert(fragment.binding.tvTotalPrice.text == "50 ₺")
            assert(fragment.binding.tvEmptyCartMessage.visibility == View.GONE)
            assert(fragment.binding.rvCartItems.visibility == View.VISIBLE)
        }
    }
}