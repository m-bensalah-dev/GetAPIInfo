package com.example.getapiinfo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageViewProduct)
        val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
        val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.titleTextView.text = product.title
        holder.categoryTextView.text = product.category
        holder.priceTextView.text = "$${product.price}"
        holder.descriptionTextView.text = product.description

        // Load image using Glide
        Glide.with(holder.itemView.context)
            .load(product.image)
            .into(holder.imageView)
    }
}
