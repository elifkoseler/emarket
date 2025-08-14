package com.elif.emarket.home

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.elif.emarket.data.local.AppDatabase
import com.elif.emarket.data.local.dao.CartDao
import com.elif.emarket.domain.entity.CartItem
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.domain.repository.CartRepository
import com.elif.emarket.domain.usecase.GetProductsUseCase
import com.elif.emarket.domain.usecase.InsertProductToLocalUseCase
import com.elif.emarket.ui.home.HomeViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    // Test dispatcher
    private val testDispatcher = UnconfinedTestDispatcher()

    // Mocks
    private val mockCartRepository = mockk<CartRepository>()
    private val mockGetProductsUseCase = mockk<GetProductsUseCase>()
    private val mockContext = mockk<Context>()
    private val mockAppDatabase = mockk<AppDatabase>()
    private val mockCartDao = mockk<CartDao>()
    private lateinit var insertProductToLocalUseCase: InsertProductToLocalUseCase

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        insertProductToLocalUseCase = InsertProductToLocalUseCase(mockCartRepository)

        // Mock dependencies
        every { mockAppDatabase.cartDao() } returns mockCartDao
        mockkObject(AppDatabase.Companion)
        every { AppDatabase.getInstance(any()) } returns mockAppDatabase

        viewModel = HomeViewModel()
        setPrivateField(viewModel, "getProductsUseCase", mockGetProductsUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `loadProducts should update products state with successful result`() = runTest {
        val expectedProducts = listOf(
            Product(id = 1, name = "Product 1", price = 100.0, description = "Desc 1", imageUrl = "image1.jpg"),
            Product(id = 2, name = "Product 2", price = 200.0, description = "Desc 2", imageUrl = "image2.jpg")
        )
        coEvery { mockGetProductsUseCase() } returns expectedProducts

        viewModel.loadProducts()

        val actualProducts = viewModel.products.first()
        assertEquals(expectedProducts, actualProducts)
        coVerify(exactly = 1) { mockGetProductsUseCase() }
    }

    @Test
    fun `loadProducts should handle empty result`() = runTest {
        val expectedProducts = emptyList<Product>()
        coEvery { mockGetProductsUseCase() } returns expectedProducts

        viewModel.loadProducts()

        val actualProducts = viewModel.products.first()
        assertTrue(actualProducts.isEmpty())
        coVerify(exactly = 1) { mockGetProductsUseCase() }
    }

    @Test
    fun `products initial state should be empty`() = runTest {
        val initialProducts = viewModel.products.first()

        assertTrue(initialProducts.isEmpty())
    }

    // Reflection helper method
    private fun setPrivateField(target: Any, fieldName: String, value: Any) {
        val field = target.javaClass.getDeclaredField(fieldName)
        field.isAccessible = true
        field.set(target, value)
    }
}