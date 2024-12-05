package com.j_and_a.demo_catalogo_eccomerce.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.adapters.ItemsAdapter
import com.j_and_a.demo_catalogo_eccomerce.adapters.RecyclerAdapterDelegate
import com.j_and_a.demo_catalogo_eccomerce.data.RecyclerViewType
import com.j_and_a.demo_catalogo_eccomerce.database.CartDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.databinding.FragmentHomeBinding
import com.j_and_a.demo_catalogo_eccomerce.model.Meal

class HomeFragment : Fragment(), RecyclerAdapterDelegate {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    /**
     * Setting Liked database for testing purposes as the first thing in the code
     * this should be done in the launch fragment and stored locale and update on every user interaction
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LikedDatabaseDemo.setAll()
        CartDatabaseDemo.setAll()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        homeViewModel.prepareDataSet()
        setupView(root, homeViewModel)
        setupObservers(root, homeViewModel)
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupView(view: View, viewModel: HomeViewModel){
       setupRecyclerView(view, viewModel)
    }

    private fun setupRecyclerView(view: View, viewModel: HomeViewModel){
        val recyclerViewNewItems : RecyclerView = view.findViewById(R.id.home_recyclerview_new)
        val recyclerViewOnSaleItems : RecyclerView = view.findViewById(R.id.home_recyclerview_sale)
        val adapterNewItems = ItemsAdapter(viewModel.testDataset, RecyclerViewType.New, this, resources)
        val adapterOnSaleItems = ItemsAdapter(viewModel.testDataset, RecyclerViewType.Sale, this, resources)
        recyclerViewNewItems.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerViewOnSaleItems.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL)
        recyclerViewNewItems.adapter = adapterNewItems
        recyclerViewOnSaleItems.adapter = adapterOnSaleItems
        //TODO move in the setupObserver function
        viewModel.updateView.observe(viewLifecycleOwner) {
            if (it){
                adapterNewItems.dataSet = viewModel.testDataset
                adapterOnSaleItems.dataSet = viewModel.testDataset
                adapterNewItems.notifyDataSetChanged()
                adapterOnSaleItems.notifyDataSetChanged()
                viewModel.updateView.value = false
            }
        }
    }

    private fun setupObservers(view: View, viewModel: HomeViewModel){
        val recyclerViewNewItems : RecyclerView = view.findViewById(R.id.home_recyclerview_new)
        val recyclerViewOnSaleItems : RecyclerView = view.findViewById(R.id.home_recyclerview_sale)
        /** updates single items based on its like status **/
        viewModel.updateItem.observe(viewLifecycleOwner){ itemPosition ->
            if (itemPosition > -1){
                recyclerViewNewItems.adapter?.notifyItemChanged(itemPosition)
                recyclerViewOnSaleItems.adapter?.notifyItemChanged(itemPosition)
                viewModel.updateItem.value = -1
            }
        }

    }

    /**
     *  Delegation of the interaction with the elements in the recyclerView
     **/

    override fun recyclerViewFunction(selectedData: Any, tag: String, position: Int) {
        val viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        val item : Meal = selectedData as Meal
        when(tag){
            "like" -> {
                viewModel.updateLike(item, position)
            }
            "item" -> {
                findNavController().navigate(R.id.fromHomepageToItemPage,
                    viewModel.getNavigationBundle(item)
                )
            }
        }
    }
}