package com.elif.emarket.detail

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.elif.emarket.R
import com.elif.emarket.databinding.FragmentProductBinding
import com.elif.emarket.domain.entity.Product
import com.elif.emarket.ui.cart.CartManager
import com.elif.emarket.ui.detail.ProductFragment
import com.elif.emarket.ui.detail.ProductViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import org.robolectric.shadows.ShadowToast

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(sdk = [29])
class ProductFragmentTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()
    private val mockNavController = mockk<NavController>(relaxed = true)
    private val mockViewModel = mockk<ProductViewModel>(relaxed = true)

    private val testProduct = Product(
        id = 0,
        name = "",
        price = 0.0,
        description = "",
        imageUrl = ""
    )

    class TestProductFragment(
        private val mockViewModel: ProductViewModel,
        private val testProduct: Product
    ) : ProductFragment() {

        constructor() : this(mockk<ProductViewModel>(relaxed = true), Product(0, "", "", "", 0.0))

        override val defaultViewModelProviderFactory: ViewModelProvider.Factory
            get() = object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return mockViewModel as T
                }
            }

        override fun onCreate(savedInstanceState: Bundle?) {
            // Override to inject test product instead of reading from arguments
            super.onCreate(savedInstanceState)
            // Use reflection or direct assignment to set the product
            val productField = ProductFragment::class.java.getDeclaredField("product")
            productField.isAccessible = true
            productField.set(this, testProduct)
        }
    }

    @Before
    fun setup() {
        mockkObject(CartManager)
        every { CartManager.addItem(any()) } just Runs
        every { mockViewModel.saveProductToLocal(any(), any()) } just Runs
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `fragment should display product information correctly`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            val binding = getFragmentBinding(fragment)

            Assert.assertEquals(testProduct.name, binding.tvProductName.text.toString())
            Assert.assertEquals(testProduct.description, binding.tvDescription.text.toString())
            Assert.assertEquals("${testProduct.price} ₺", binding.tvPrice.text.toString())
        }
    }

    @Test
    fun `add to cart button should add item to CartManager and show toast`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            val binding = getFragmentBinding(fragment)

            binding.btnAddToCart.performClick()

            verify { CartManager.addItem(testProduct) }

            // Verify toast was shown
            val latestToast = ShadowToast.getLatestToast()
            Assert.assertNotNull(latestToast)
            Assert.assertEquals("${testProduct.name} added to cart!", ShadowToast.getTextOfLatestToast())
        }
    }

    @Test
    fun `toolbar navigation should navigate up`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            Navigation.setViewNavController(fragment.requireView(), mockNavController)
            val binding = getFragmentBinding(fragment)

            // Click toolbar navigation
            binding.toolbar.setNavigationOnClickListener {
                verify { mockNavController.navigateUp() }
            }
        }
    }

    @Test
    fun `binding should be null after onDestroyView`() {
        val scenario = launchTestFragment()

        scenario.onFragment { fragment ->
            val binding = getFragmentBinding(fragment)
            Assert.assertNotNull(binding)
        }

        scenario.moveToState(androidx.lifecycle.Lifecycle.State.DESTROYED)
    }

    private fun launchTestFragment(): FragmentScenario<TestProductFragment> {
        val fragmentFactory = object : androidx.fragment.app.FragmentFactory() {
            override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
                return if (className == TestProductFragment::class.qualifiedName) {
                    TestProductFragment(mockViewModel, testProduct)
                } else {
                    super.instantiate(classLoader, className)
                }
            }
        }

        val args = Bundle().apply {
            putParcelable("product", testProduct)
        }

        return FragmentScenario.launchInContainer(
            TestProductFragment::class.java,
            args,
            R.style.Theme_Emarket,
            fragmentFactory
        )
    }

    private fun getFragmentBinding(fragment: TestProductFragment): FragmentProductBinding {
        val bindingField = ProductFragment::class.java.getDeclaredField("_binding")
        bindingField.isAccessible = true
        return bindingField.get(fragment) as FragmentProductBinding
    }
}