package com.j_and_a.demo_catalogo_eccomerce.ui.shop_category

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.adapters.CategoriesAdapter
import com.j_and_a.demo_catalogo_eccomerce.adapters.RecyclerAdapterDelegate
import com.j_and_a.demo_catalogo_eccomerce.databinding.FragmentShopCategoriesBinding
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem

class ShopCategoryFragment : Fragment(), RecyclerAdapterDelegate {

    private var _binding: FragmentShopCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ViewModelProvider(this)[ShopCategoryViewModel::class.java].fetchCategories()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val shopViewModel = ViewModelProvider(this)[ShopCategoryViewModel::class.java]
        _binding = FragmentShopCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupView(root, shopViewModel)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(view: View, viewModel: ShopCategoryViewModel){
        setupRecyclerView(view, viewModel)
    }

    private fun setupRecyclerView(view: View, viewModel: ShopCategoryViewModel){
        val recyclerView: RecyclerView = view.findViewById(R.id.shop_categories_recyclerView_categories)
        val adapter = CategoriesAdapter(viewModel.listCategories.value!!, this, resources)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        //TODO move in the setupObservers function
        viewModel.listCategories.observe(viewLifecycleOwner){
            adapter.dataSet = it
            adapter.notifyDataSetChanged()
        }

    }

    /**
     *  Delegation of the interaction with the elements in the recyclerView
     **/
    override fun recyclerViewFunction(selectedData: Any, tag: String, position: Int) {
        val shopViewModel = ViewModelProvider(this)[ShopCategoryViewModel::class.java]
        val selectedCategoryItem : CategoryItem = selectedData as CategoryItem
        findNavController().navigate(R.id.fromShopCategoriesToShopItems,
            shopViewModel.getNavigationBundle(selectedCategoryItem)
        )
    }
}