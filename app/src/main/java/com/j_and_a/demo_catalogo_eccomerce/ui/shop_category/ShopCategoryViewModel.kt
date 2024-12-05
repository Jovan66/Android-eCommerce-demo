package com.j_and_a.demo_catalogo_eccomerce.ui.shop_category

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.j_and_a.demo_catalogo_eccomerce.api.MealAPI
import com.j_and_a.demo_catalogo_eccomerce.api.RetrofitInstance
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryListGson
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ShopCategoryViewModel : ViewModel() {

    private val _listCategories = MutableLiveData<ArrayList<CategoryItem>>().apply {
        value = ArrayList<CategoryItem>()
    }

    val listCategories: LiveData<ArrayList<CategoryItem>> = _listCategories


    fun fetchCategories(){
        RetrofitInstance.api.getCategories().enqueue(object: Callback<CategoryListGson> {
            override fun onResponse(p0: Call<CategoryListGson>, p1: Response<CategoryListGson>) {
                p1.body().let { body ->
                    _listCategories.postValue(ArrayList(body!!.categories))
                }
            }
            override fun onFailure(p0: Call<CategoryListGson>, p1: Throwable) {
                Log.d("ShopCategoryViewModel",p1.message.toString())//"Error while fetching categories")
                Log.d("ShopCategoryViewModel","Error while fetching categories")
            }
        })
    }

    /**
     *  Setting the data in the bundle to correctly lode the next fragment
     **/
    fun getNavigationBundle(categoryItem: CategoryItem) : Bundle {
        val bundle = Bundle()
        bundle.putString("CATEGORY_NAME", categoryItem.strCategory)
        bundle.putString("CATEGORY_ID",categoryItem.idCategory)
        return bundle
    }
}