package com.j_and_a.demo_catalogo_eccomerce.adapters

import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Space
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem
import com.j_and_a.demo_catalogo_eccomerce.utilities.Graphics
import com.squareup.picasso.Picasso

class CategoriesAdapter(
    var dataSet: ArrayList<CategoryItem>,
    private val delegate: RecyclerAdapterDelegate,
    private val resources: Resources
) : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val holderView: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_categories_layout, parent, false)
        return ViewHolder(view)
    }

    //TODO Divide in section like other fragments View or adapters
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.holderView
        val currentItem: CategoryItem = dataSet[position]

        val spaceVisibility = if (position==dataSet.size-1){ View.GONE } else { View.VISIBLE }
        view.findViewById<Space>(R.id.recyclerView_categories_space).visibility = spaceVisibility

        val linerLayout : LinearLayout = view.findViewById(R.id.recyclerView_categories_linerLayout)
        val textView: TextView = view.findViewById(R.id.recyclerView_categories_textView)
        val imageView: ImageView = view.findViewById(R.id.recyclerView_categories_imageView)
        linerLayout.clipToOutline = true

        //TODO The first category is always a mock for sales category, this code makes its design pop-of
        if(position==0){
            linerLayout.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))
            textView.setTextColor(resources.getColor(R.color.white, null))
            textView.gravity = Gravity.CENTER
            val text = "SUMMER SALES"
            textView.text = text
            imageView.visibility = View.GONE
        }else {
            linerLayout.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.white, null))
            textView.setTextColor(resources.getColor(R.color.black, null))
            textView.gravity = Gravity.START
            textView.gravity = Gravity.CENTER_VERTICAL
            textView.text = currentItem.strCategory
            imageView.visibility = View.VISIBLE
            Picasso.get().load(currentItem.strCategoryThumb).into(imageView)
        }

        view.setOnClickListener { delegate.recyclerViewFunction(currentItem, currentItem.strCategory, position) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun getItem(index: Int): CategoryItem { return dataSet[index] }
}