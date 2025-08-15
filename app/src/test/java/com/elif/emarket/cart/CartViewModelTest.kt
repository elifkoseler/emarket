package com.elif.emarket.cart

import android.annotation.SuppressLint
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elif.emarket.domain.model.CartItem
import com.elif.emarket.domain.repository.CartRepository
import com.elif.emarket.ui.cart.CartViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CartViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var repository: CartRepository
    private lateinit var viewModel: CartViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk()

        every { repository.getCartProducts() } returns flowOf(emptyList())

        viewModel = CartViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `insertAll should call repository insertAll`() = runTest {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 1),
            CartItem(productName = "Product 2", price = 20, quantity = 2),
        )
        coEvery { repository.insertAll(any()) } returns Unit

        viewModel.insertAll(cartItems)

        coVerify { repository.insertAll(cartItems) }
    }

    @Test
    fun `updateItem should call repository updateProduct`() = runTest {
        val cartItem = CartItem(productName = "Product 1", price = 10, quantity = 1)
        coEvery { repository.updateProduct(any()) } returns Unit

        viewModel.updateItem(cartItem)

        coVerify { repository.updateProduct(cartItem) }
    }

    @Test
    fun `deleteItem should call repository deleteProduct`() = runTest {
        val cartItem = CartItem(productName = "Product 1", price = 10, quantity = 1)
        coEvery { repository.deleteProduct(any()) } returns Unit

        viewModel.deleteItem(cartItem)

        coVerify { repository.deleteProduct(cartItem) }
    }

    @Test
    fun `clearCart should call repository clearCart`() = runTest {
        coEvery { repository.clearCart() } returns Unit

        viewModel.clearCart()

        coVerify { repository.clearCart() }
    }

    @Test
    fun `saveCartToRoom should clear cart and insert items`() = runTest {
        val cartItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 1),
            CartItem(productName = "Product 2", price = 20, quantity = 2)
        )
        coEvery { repository.clearCart() } returns Unit
        coEvery { repository.insertAll(any()) } returns Unit

        viewModel.saveCartToRoom(cartItems)

        coVerify { repository.clearCart() }
        coVerify { repository.insertAll(cartItems) }
    }

    @SuppressLint("CheckResult")
    @Test
    fun `loadCartFromRoom should return LiveData from repository`() {
        val expectedItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 1),
        )
        every { repository.getCartProducts() } returns flowOf(expectedItems)

        viewModel.loadCartFromRoom()

        verify { repository.getCartProducts() }
    }

    @SuppressLint("CheckResult")
    @Test
    fun `cartItems should return LiveData from repository`() {
        val expectedItems = listOf(
            CartItem(productName = "Product 1", price = 10, quantity = 1),
        )
        every { repository.getCartProducts() } returns flowOf(expectedItems)

        val cartItems = viewModel.cartItems

        verify { repository.getCartProducts() }
        assert(cartItems != null)

    }
}
