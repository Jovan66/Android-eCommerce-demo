package com.j_and_a.demo_catalogo_eccomerce.adapters

import android.app.Activity
import android.content.res.Resources
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Space
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.data.RecyclerViewType
import com.j_and_a.demo_catalogo_eccomerce.database.CartDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.utilities.Graphics
import com.squareup.picasso.Picasso
import kotlin.random.Random

/**
 * This parameters are for convenience and ease in graphic styling
 **/

class ItemsAdapter(
    var dataSet: ArrayList<Meal>,
    private val type: RecyclerViewType,
    private val delegate: RecyclerAdapterDelegate,
    private val resources: Resources,
    private val screenWith: Int? = null,
    private val activity: Activity? = null
) : RecyclerView.Adapter<ItemsAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val holderView: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_homepage_items_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val view = holder.holderView
        val currentItem: Meal = dataSet[position]
        setUpView(view, currentItem, position)
    }

    private fun setUpView(view: View, item: Meal, position: Int){
        setupSpaces(view, item, position)
        setupTexts(view, item, position)
        setupImages(view, item, position)
        setupOnClickListeners(view, item, position)
    }

    /**
     *  This setups the space so that one adapter can fit different layouts
     *  Later different adapters can be extrapolated, for now is this implementation saves time
     *
     *  The parameters above are used to understand what type of layout is needed
     **/

    private fun setupSpaces(view: View, item: Meal, position: Int){
        val spaceRight : Space = view.findViewById<Space>(R.id.recycler_space_divider)
        val spaceBottom : Space = view.findViewById<Space>(R.id.recycler_space_bottom)
        spaceBottom.visibility = View.GONE
        if (position==dataSet.size-1 && screenWith == null){
            spaceRight.visibility = View.GONE
            spaceBottom.visibility = View.GONE
        }
        if (screenWith!=null){
            if (position%2 != 0){
                spaceRight.visibility = View.GONE
            }else {
                spaceRight.visibility = View.VISIBLE
            }
            if (position==dataSet.size-1 || (position==dataSet.size-2 && dataSet.size%2 == 0)){
                spaceBottom.visibility = View.GONE
            }else{
                spaceBottom.visibility = View.VISIBLE
            }
        }
    }

    /**
     * This can be perfected and cleaned but we don't know the final logic yet
     * */
    private fun setupTexts(view: View, item: Meal, position: Int){
        val cornerText : TextView = view.findViewById<TextView>(R.id.recycler_layout_textview_corner)
        val itemNameText : TextView = view.findViewById<TextView>(R.id.recycler_layout_textview_itemname)
        val itemCategoryText : TextView = view.findViewById<TextView>(R.id.recycler_layout_textview_category)
        val itemPriceText : TextView = view.findViewById<TextView>(R.id.recycler_layout_textview_price)
        val itemDiscountText : TextView = view.findViewById<TextView>(R.id.recycler_layout_textview_sale)

        itemNameText.text = item.strMeal
        itemCategoryText.text = item.strCategory

        //TODO Remove price hardcoded
        val price: Double = if (item.price!=null) item.price!! else Random.nextDouble(7.99, 42.99)

        val priceText = "${price.toInt()}€"
        itemPriceText.text = priceText
        itemPriceText.setTextColor(resources.getColor(R.color.black, null))
        itemPriceText.paintFlags = 0

        itemDiscountText.visibility = View.GONE

        /**
         * Type is used to customize the top left text, orientation and design
         **/
        when(type){
            RecyclerViewType.Normal     -> {
                cornerText.visibility = View.GONE
            }
            RecyclerViewType.New        -> {
                cornerText.visibility = View.VISIBLE
                val newText = "NEW"
                cornerText.text = newText
                cornerText.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.black, null))
            }
            RecyclerViewType.Sale -> {
                val discount = Random.nextInt(10, 35)
                cornerText.visibility = View.VISIBLE
                //TODO Remove discount hardcoded
                val saleText = "-$discount%"
                cornerText.text = saleText
                cornerText.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))
                itemPriceText.setTextColor(resources.getColor(R.color.text_gray, null))
                itemPriceText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                itemDiscountText.visibility = View.VISIBLE
                val discountedPriceText = "${(price.minus((price * (discount/100.0)))).toInt()}€"
                itemDiscountText.text = discountedPriceText
            }
            RecyclerViewType.Like -> {
                cornerText.visibility = View.GONE
            }
        }
    }

    /**
     * Logic for the image and like/car button appearance
     **/
    private fun setupImages(view: View, item: Meal, position: Int){
        val imageView: ImageView = view.findViewById(R.id.recycler_layout_imageview_image)
        setWith(imageView)
        imageView.clipToOutline = true
        Picasso.get().load(item.strMealThumb).into(imageView)

        val imageButtonLike: ImageButton = view.findViewById(R.id.recycler_layout_imageview_likes)
        imageButtonLike.visibility = View.VISIBLE
        if (type != RecyclerViewType.Like){
            if (LikedDatabaseDemo.likedList.contains(item.strMeal)){
                imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.like_full_color, null))
                imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))

            }else{
                imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.like, null))
                imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.text_gray, null))
            }
        }else{
            if (CartDatabaseDemo.cartList.contains(item.strMeal)){
                imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.shopping_cart_full, null))
                imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.white, null))
                imageButtonLike.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))
            }else{
                imageButtonLike.visibility = View.GONE
            }
        }

    }

    /**
     *  Sets the width in case of spanCount 2 in recyclerView
     **/
    private fun setWith(view: ImageView){
        if (activity!=null && screenWith!=null) {
            val params = view.layoutParams
            val newImageWith = (screenWith - Graphics.dpToPx(activity.applicationContext, 20))/2
            params.width = newImageWith
            view.layoutParams = params
        }
    }

    /**
     *  The action for the clicks are handle in the view in witch the recyclerView belongs
     *  Tags are assigned as identifier for the delegate
     **/
    private fun setupOnClickListeners(view: View, item: Meal, position: Int){
        view.setOnClickListener { delegate.recyclerViewFunction(item, "item", position) }
        view.findViewById<ImageButton>(R.id.recycler_layout_imageview_likes).setOnClickListener { delegate.recyclerViewFunction(item, "like", position) }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun getItem(index: Int): Meal { return dataSet[index] }

}