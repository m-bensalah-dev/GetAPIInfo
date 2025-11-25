package com.example.getapiinfo

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var spinnerCategories: Spinner
    private lateinit var recyclerViewProducts: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var allProducts: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinnerCategories = findViewById(R.id.spinnerCategories)
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts)
        recyclerViewProducts.layoutManager = GridLayoutManager(this, 2)

        // Fetch products from API
        fetchProducts()
    }

    private fun fetchProducts() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitObj.api.getAllProducts()
                if (response.isSuccessful) {
                    allProducts = response.body() ?: emptyList()

                    // Back to main thread to update UI
                    withContext(Dispatchers.Main) {
                        // Show all products in RecyclerView
                        productAdapter = ProductAdapter(allProducts)
                        recyclerViewProducts.adapter = productAdapter

                        // Setup Spinner with categories
                        setupSpinner()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setupSpinner() {
        // Get unique categories
        val categories = allProducts.map { it.category }.distinct()

        // Create ArrayAdapter for Spinner
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        // Handle Spinner selection
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filterProductsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Show all products if nothing selected
                productAdapter = ProductAdapter(allProducts)
                recyclerViewProducts.adapter = productAdapter
            }
        }
    }

    private fun filterProductsByCategory(category: String) {
        val filtered = allProducts.filter { it.category == category }
        productAdapter = ProductAdapter(filtered)
        recyclerViewProducts.adapter = productAdapter
    }
}
