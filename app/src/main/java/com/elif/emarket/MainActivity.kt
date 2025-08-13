package com.elif.emarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.elif.emarket.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.post {
            setupBottomNavigation()
        }
    }

    private fun setupBottomNavigation() {
        try {
            val navController = findNavController(R.id.nav_host_fragment)
            binding.bottomNavigation.setupWithNavController(navController)

            binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.navigation_home -> {
                        navController.navigate(R.id.navigation_home)
                        true
                    }
                    R.id.navigation_cart -> {
                        navController.navigate(R.id.navigation_cart)
                        true
                    }
                    else -> false
                }
            }
        } catch (e: IllegalStateException) {
            binding.root.postDelayed({
                setupBottomNavigation()
            }, 100)
        }
    }
}