package com.j_and_a.demo_catalogo_eccomerce.adapters

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.squareup.picasso.Picasso
import kotlin.random.Random

class CartItemsAdapter(
    var dataSet: ArrayList<Meal>,
    private val delegate: RecyclerAdapterDelegate,
    private val resources: Resources
) : RecyclerView.Adapter<CartItemsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val holderView: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_cart_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.holderView
        val currentItem: Meal = dataSet[position]
        setUpView(view, currentItem, position)
    }

    private fun setUpView(view: View, item: Meal, position: Int){
        val linerLayout : LinearLayout = view.findViewById(R.id.recyclerView_cart_linerLayout)
        linerLayout.clipToOutline = true

        setupSpaces(view, item, position)
        setupTexts(view, item, position)
        setupImages(view, item, position)
    }

    private fun setupSpaces(view: View, item: Meal, position: Int){
        val spaceBottom : Space = view.findViewById<Space>(R.id.recyclerView_cart_spaceBottom)
        val spaceVisibility = if (position==dataSet.size-1){ View.GONE } else { View.VISIBLE }
        spaceBottom.visibility = spaceVisibility
    }
    private fun setupTexts(view: View, item: Meal, position: Int){
        val itemNameTextView    : TextView = view.findViewById(R.id.recyclerView_cart_textView_itemName)
        val coloLabelTextView   : TextView = view.findViewById(R.id.recyclerView_cart_textView_coloLabel)
        val colorTextView       : TextView = view.findViewById(R.id.recyclerView_cart_textView_coloText)
        val sizeLabelTextView   : TextView = view.findViewById(R.id.recyclerView_cart_textView_sizeLabel)
        val sizeTextView        : TextView = view.findViewById(R.id.recyclerView_cart_textView_sizeText)
        val amountTextView      : TextView = view.findViewById(R.id.recyclerView_cart_textView_amount)
        val priceTextView       : TextView = view.findViewById(R.id.recyclerView_cart_textView_price)

        itemNameTextView.text = item.strMeal
        //TODO Remove price hardcoded
        val price: Double = if (item.price!=null) item.price!! else Random.nextDouble(7.99, 42.99)
        val priceText = "${price.toInt()}€"
        priceTextView.text = priceText
    }

    private fun setupImages(view: View, item: Meal, position: Int){
        val imageView: ImageView = view.findViewById(R.id.recyclerView_cart_imageView)
        Picasso.get().load(item.strMealThumb).into(imageView)
    }


    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun getItem(index: Int): Meal { return dataSet[index] }

}