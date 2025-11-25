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


        fetchProducts()
    }

    private fun fetchProducts() {
        lifecycleScope.launch(Dispatchers.IO) {
            val response = RetrofitObj.api.getAllProducts()
            if (response.isSuccessful) {
                allProducts = response.body() ?: emptyList()

                // Retourn  Back to UI thread pour mise ajour recycle view et spinner
                withContext(Dispatchers.Main) {
                    productAdapter = ProductAdapter(allProducts)
                    recyclerViewProducts.adapter = productAdapter

                    setupSpinner()
                }
            }
        }
    }


    private fun setupSpinner() {
        // pick unique categories to for avoiding repeated values
        val categories = allProducts.map { it.category }.distinct()

        // Creation ArrayAdapter for Spinner
        val spinnerAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            categories
        )
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategories.adapter = spinnerAdapter

        // un spineer a ete selectione.
        spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                filterProductsByCategory(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // afficher tout les produits par defaut si aucun produit a ete selectionne
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
