package com.j_and_a.demo_catalogo_eccomerce.ui.liked_items

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
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.databinding.FragmentLikedBinding
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.utilities.Graphics

class LikedFragment : Fragment(), RecyclerAdapterDelegate {

    private var _binding: FragmentLikedBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this)[LikedViewModel::class.java]
        viewModel.fetchMeals(LikedDatabaseDemo.likedList)
        _binding = FragmentLikedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupView(root, viewModel)
        return root
    }

    private fun setupView(view: View, viewModel: LikedViewModel){
        setupRecyclerView(view, viewModel)
    }

    private fun setupRecyclerView(view: View, viewModel: LikedViewModel){
        val recyclerView : RecyclerView = view.findViewById(R.id.liked_recyclerView_items)
        val adapter = ItemsAdapter(viewModel._listMeals.value!!, RecyclerViewType.Like, this, resources, Graphics.getScreenWith(activity), activity)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        viewModel._listMeals.observe(viewLifecycleOwner) {
            adapter.dataSet = it
            adapter.notifyDataSetChanged()
        }
    }

    /**
     *  Delegation of the interaction with the elements in the recyclerView
     **/
    override fun recyclerViewFunction(selectedData: Any, tag: String, position: Int) {
        val viewModel = ViewModelProvider(this)[LikedViewModel::class.java]
        val item : Meal = selectedData as Meal
        when(tag){
            "item" -> {
                findNavController().navigate(R.id.fromLikesToItemPage,
                    viewModel.getNavigationBundle(item)
                )
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}