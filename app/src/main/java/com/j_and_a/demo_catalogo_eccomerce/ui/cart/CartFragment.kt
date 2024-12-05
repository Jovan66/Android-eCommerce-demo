package com.j_and_a.demo_catalogo_eccomerce.ui.cart

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.j_and_a.demo_catalogo_eccomerce.R
import com.j_and_a.demo_catalogo_eccomerce.adapters.CartItemsAdapter
import com.j_and_a.demo_catalogo_eccomerce.adapters.RecyclerAdapterDelegate
import com.j_and_a.demo_catalogo_eccomerce.database.CartDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.databinding.FragmentCartBinding

/**
 *  This Fragment is mostly incomplete
 **/

class CartFragment : Fragment(), RecyclerAdapterDelegate {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        viewModel.fetchMeals(CartDatabaseDemo.cartList)
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupView(root, viewModel)
        return root
    }

    private fun setupView(view: View, viewModel: CartViewModel){
        setupRecyclerView(view, viewModel)
    }

    private fun setupRecyclerView(view: View, viewModel: CartViewModel){
        val recyclerView : RecyclerView = view.findViewById(R.id.cart_recyclerView_items)
        val adapter = CartItemsAdapter(viewModel._listMeals.value!!, this, resources)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
        viewModel._listMeals.observe(viewLifecycleOwner) {
            adapter.dataSet = it
            adapter.notifyDataSetChanged()
        }

    }
    /**
     *  Delegation of the interaction with the elements in the recyclerView
     **/
    /*  TODO implement add/remove quantity
        TODO add dots menu
        TODO add click on item image to open item
     */
    override fun recyclerViewFunction(selectedData: Any, tag: String, position: Int) {

    }
}