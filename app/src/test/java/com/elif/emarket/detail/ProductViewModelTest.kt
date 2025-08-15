package com.elif.emarket.detail

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elif.emarket.data.local.AppDatabase
import com.elif.emarket.data.local.dao.CartDao
import com.elif.emarket.data.repository.CartRepositoryImpl
import com.elif.emarket.domain.model.Product
import com.elif.emarket.domain.usecase.InsertProductToLocalUseCase
import com.elif.emarket.ui.detail.ProductViewModel
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
class ProductViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private lateinit var viewModel: ProductViewModel

    private val testProduct = Product(
        id = 1,
        name = "Test Product",
        price = 150.0,
        description = "Test Description",
        imageUrl = "test_image.jpg"
    )

    @Before
    fun setup() {
        mockkObject(AppDatabase.Companion)
        mockkConstructor(CartRepositoryImpl::class)
        mockkConstructor(InsertProductToLocalUseCase::class)

        val mockDao = mockk<CartDao>(relaxed = true)
        val mockDatabase = mockk<AppDatabase>(relaxed = true)
        every { mockDatabase.cartDao() } returns mockDao
        every { AppDatabase.getInstance(any()) } returns mockDatabase

        coEvery { anyConstructed<InsertProductToLocalUseCase>().invoke(any()) } returns Unit

        viewModel = ProductViewModel()
    }

    @After
    fun tearDown() {
        clearAllMocks()
        unmockkAll()
    }

    @Test
    fun `saveProductToLocal should create database instance with context`() = runTest(testDispatcher) {
        val mockContext = mockk<Context>(relaxed = true) {
            every { applicationContext } returns this
        }

        viewModel.saveProductToLocal(testProduct, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        verify { AppDatabase.getInstance(mockContext) }
    }

    @Test
    fun `saveProductToLocal should call InsertProductToLocalUseCase`() = runTest(testDispatcher) {
        val mockContext = mockk<Context>(relaxed = true) {
            every { applicationContext } returns this
        }

        viewModel.saveProductToLocal(testProduct, mockContext)
        testDispatcher.scheduler.advanceUntilIdle()

        coVerify { anyConstructed<InsertProductToLocalUseCase>().invoke(testProduct) }
    }
}