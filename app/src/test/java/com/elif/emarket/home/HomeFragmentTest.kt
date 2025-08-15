package com.elif.emarket.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elif.emarket.R
import com.elif.emarket.domain.model.Product
import com.elif.emarket.ui.cart.CartManager
import com.elif.emarket.ui.home.HomeFragment
import com.elif.emarket.ui.home.HomeFragmentDirections
import com.elif.emarket.ui.home.HomeViewModel
import com.elif.emarket.ui.home.adapter.ProductAdapter
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
class HomeFragmentTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val mockNavController = mockk<NavController>(relaxed = true)

    private val testProducts = listOf(
        Product(
            id = 1,
            name = "Product 1",
            price = 100.0,
            description = "Desc 1",
            imageUrl = "image1.jpg"
        ),
        Product(
            id = 2,
            name = "Product 2",
            price = 200.0,
            description = "Desc 2",
            imageUrl = "image2.jpg"
        )
    )

    private val productsFlow = MutableStateFlow(testProducts)

    class TestHomeFragment(
        private val mockViewModel: HomeViewModel
    ) : HomeFragment() {

        // Default constructor for fragment recreation
        constructor() : this(mockk<HomeViewModel>(relaxed = true))

        override val defaultViewModelProviderFactory: ViewModelProvider.Factory
            get() = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return mockViewModel as T
                }
            }
    }

    @Before
    fun setup() {
        mockkObject(CartManager)
        every { CartManager.addItem(any()) } just Runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `fragment should initialize RecyclerView with GridLayoutManager`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.rvProducts)
            Assert.assertNotNull(recyclerView)
            Assert.assertTrue(recyclerView?.layoutManager is GridLayoutManager)
            Assert.assertEquals(2, (recyclerView?.layoutManager as GridLayoutManager).spanCount)
        }
    }

    @Test
    fun `fragment should set adapter to RecyclerView`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            val recyclerView = fragment.view?.findViewById<RecyclerView>(R.id.rvProducts)
            Assert.assertNotNull(recyclerView?.adapter)
            Assert.assertTrue(recyclerView?.adapter is ProductAdapter)
        }
    }

    @Test
    fun `ProductAdapter onAddToCartClick should add item to CartManager`() {
        val testProduct = testProducts[0]
        val scenario = launchTestFragment()

        scenario.onFragment {
            (it.view?.findViewById<RecyclerView>(R.id.rvProducts)?.adapter as ProductAdapter)
                .onAddToCartClick.invoke(testProduct)
            verify { CartManager.addItem(testProduct) }
        }
    }

    @Test
    fun `ProductAdapter onProductClick should navigate to ProductFragment`() {
        val testProduct = testProducts[0]
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            (fragment.view?.findViewById<RecyclerView>(R.id.rvProducts)?.adapter as ProductAdapter)
                .onProductClick.invoke(testProduct)

            verify {
                mockNavController.navigate(
                    HomeFragmentDirections.actionHomeFragmentToProductFragment(testProduct)
                )
            }
        }
    }

    private fun launchTestFragment(): FragmentScenario<TestHomeFragment> {
        val mockViewModel = mockk<HomeViewModel>(relaxed = true)
        every { mockViewModel.products } returns productsFlow
        every { mockViewModel.loadProducts() } just Runs
        every { mockViewModel.saveProductToLocal(any(), any()) } just Runs

        val fragmentFactory = object : FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return if (className == TestHomeFragment::class.qualifiedName) {
                    TestHomeFragment(mockViewModel)
                } else {
                    super.instantiate(classLoader, className)
                }
            }
        }

        return FragmentScenario.launchInContainer(
            TestHomeFragment::class.java,
            null,
            R.style.Theme_Emarket,
            fragmentFactory
        )
    }
}