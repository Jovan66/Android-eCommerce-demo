package com.j_and_a.demo_catalogo_eccomerce.ui.item_page

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.database.CartDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.databinding.FragmentItemPageBinding
import com.j_and_a.demo_catalogo_eccomerce.utilities.Graphics
import com.squareup.picasso.Picasso


/**
 *  This fragment is incomplete
 *  TODO add bottom sheet with full item customization before adding it to the cart
 **/

class ItemPageFragment : Fragment() {

    private var _binding: FragmentItemPageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Retrieving the item id from the bundle
     **/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {bundle ->
            ViewModelProvider(this)[ItemPageViewModel::class.java].fetchItemDetails(bundle?.getString("ITEM_ID")?: "")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemPageBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val viewModel = ViewModelProvider(this)[ItemPageViewModel::class.java]
        if (viewModel.currentItem.value!=null) setupView(root, viewModel)
        setupObservers(root, viewModel)
        setupOnClickListeners(root, viewModel)

        return root
    }

    private fun setupView(view: View, viewModel: ItemPageViewModel){
        setupText(view, viewModel)
        setupImages(view, viewModel)
    }

    private fun setupText(view: View, viewModel: ItemPageViewModel){
        val item = viewModel.currentItem.value
        binding.itemPageTextViewItemName.text = item?.strMeal
        binding.itemPageTextViewItemCategory.text = item?.strCategory
        val extraInfoString = "Country of origin: " + item?.strArea
        binding.itemPageTextViewItemExtraInfo.text = extraInfoString
        binding.itemPageTextViewItemDescription.text = item?.strInstructions
    }

    private fun setupImages(view: View, viewModel: ItemPageViewModel){
        val item = viewModel.currentItem.value
        Picasso.get().load(item?.strMealThumb).into(binding.itemPageImageViewItemImage)
        binding.itemPageImageViewItemImage.clipToOutline = true

        setupLikeButton(view, viewModel)
    }

    /**
     * Logic for the image and like/car button appearance
     **/

    private fun setupLikeButton(view: View, viewModel: ItemPageViewModel){
        val item = viewModel.currentItem.value
        val imageButtonLike = binding.itemPageImageButtonLikes
        imageButtonLike.visibility = View.VISIBLE
        if (CartDatabaseDemo.cartList.contains(item?.strMeal)){
            imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.shopping_cart_full, null))
            imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.white, null))
            imageButtonLike.backgroundTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))
        }
        else if (LikedDatabaseDemo.likedList.contains(item?.strMeal)) {
            imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.like_full_color, null))
            imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.main_red, null))
        }else{
            imageButtonLike.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.like, null))
            imageButtonLike.imageTintList = Graphics.getStateList(resources.getColor(R.color.text_gray, null))
        }
    }

    private fun setupOnClickListeners(view: View, viewModel: ItemPageViewModel){
        binding.itemPageImageButtonLikes.setOnClickListener {
            viewModel.updateLike()
        }
    }

    private fun setupObservers(view: View, viewModel: ItemPageViewModel){
        viewModel.currentItem.observe(viewLifecycleOwner) {meal ->
            meal.let { setupView(view, viewModel) }

        }
        viewModel.updateView.observe(viewLifecycleOwner){ toUpdate ->
            if (toUpdate){
                viewModel.updateView.value = false
                setupLikeButton(view, viewModel)
            }
        }
    }
}