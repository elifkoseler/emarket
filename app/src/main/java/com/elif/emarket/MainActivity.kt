package com.elif.emarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.elif.emarket.databinding.ActivityMainBinding
import com.elif.emarket.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        initViews()
        setupListeners()
    }
    
    private fun initViews() {
        loadFragment(HomeFragment())
    }
    
    private fun setupListeners() {
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_home -> {
                    loadFragment(HomeFragment())
                    true
                }
                R.id.navigation_cart -> {
                    // TODO: Load cart fragment
                    true
                }
                R.id.navigation_favorites -> {
                    // TODO: Load favorites fragment
                    true
                }
                R.id.navigation_profile -> {
                    // TODO: Load profile fragment
                    true
                }
                else -> false
            }
        }
    }
    
    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
} 