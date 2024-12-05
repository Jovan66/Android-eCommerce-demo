package com.j_and_a.demo_catalogo_eccomerce.ui.shop_items

import android.app.Activity
import android.graphics.Insets
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.adapters.ItemsAdapter
import com.j_and_a.demo_catalogo_eccomerce.adapters.RecyclerAdapterDelegate
import com.j_and_a.demo_catalogo_eccomerce.data.RecyclerViewType
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.utilities.Graphics


class ShopItemsFragment : Fragment(), RecyclerAdapterDelegate {

    companion object {
        fun newInstance() = ShopItemsFragment()
    }

    /**
     * Retrieving the item id from the bundle
     **/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_shop_items, container, false)
        val viewModel: ShopItemsViewModel = ViewModelProvider(this)[ShopItemsViewModel::class.java]
        arguments.let {bundle ->
            viewModel.fetchMeals((bundle?.getString("CATEGORY_NAME"))?: "")
        }
        setupView(view, viewModel)
        setupObservers(view, viewModel)
        return view
    }

    private fun setupView(view: View, viewModel: ShopItemsViewModel){
        setupRecyclerView(view, viewModel)
    }

    private fun setupRecyclerView(view: View, viewModel: ShopItemsViewModel){
        val recyclerView : RecyclerView = view.findViewById(R.id.shop_items_recyclerView_items)
        val adapter = ItemsAdapter(viewModel._listMeals.value!!, RecyclerViewType.Normal, this, resources, Graphics.getScreenWith(activity), activity)
        recyclerView.adapter = adapter
        //TODO move in the setupObservers function
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewModel._listMeals.observe(viewLifecycleOwner) {
            adapter.dataSet = it
            adapter.notifyDataSetChanged()
        }
    }

    private fun setupObservers(view: View, viewModel: ShopItemsViewModel){
        /** updates single items based on its like status **/
        viewModel.updateItem.observe(viewLifecycleOwner){ itemPosition ->
            if (itemPosition > 0){
                val recyclerView : RecyclerView = view.findViewById(R.id.shop_items_recyclerView_items)
                recyclerView.adapter?.notifyItemChanged(itemPosition)
                viewModel.updateItem.value = -1
            }
        }
    }

    /**
     *  Delegation of the interaction with the elements in the recyclerView
     **/
    override fun recyclerViewFunction(selectedData: Any, tag: String, position: Int) {
        val viewModel: ShopItemsViewModel = ViewModelProvider(this)[ShopItemsViewModel::class.java]
        val item : Meal = selectedData as Meal
        when(tag){
            "like" -> { viewModel.updateLike(item, position) }
            "item" -> {
                findNavController().navigate(R.id.fromShopItemsToItemPage,
                    viewModel.getNavigationBundle(item)
                )
            }
        }
    }
}