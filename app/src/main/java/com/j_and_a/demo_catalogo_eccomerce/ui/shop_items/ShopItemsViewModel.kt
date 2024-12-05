package com.j_and_a.demo_catalogo_eccomerce.ui.shop_items

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.j_and_a.demo_catalogo_eccomerce.api.RetrofitInstance
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryListGson
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.model.MealSearchGson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopItemsViewModel : ViewModel() {
    val _listMeals = MutableLiveData<ArrayList<Meal>>().apply { value = ArrayList<Meal>() }
    val updateItem = MutableLiveData<Int>().apply { value = -1 }

    fun fetchMeals(categoryName: String){
        RetrofitInstance.api.getAllMealsByCategory(categoryName).enqueue(object: Callback<MealSearchGson> {
            override fun onResponse(p0: Call<MealSearchGson>, p1: Response<MealSearchGson>) {
                p1.body().let { body ->
                    _listMeals.postValue(body!!.meals)
                }
            }
            override fun onFailure(p0: Call<MealSearchGson>, p1: Throwable) {
                Log.d("ShopItemViewModel",p1.message.toString())
                Log.d("ShopItemViewModel","Error while fetching Meals in ShopItemFragment")
            }
        })
    }

    /**
     *  Like database update logic
     *  This should be moved on the database logic file
     **/
    fun updateLike(item: Meal, position: Int){
        if(LikedDatabaseDemo.likedList.contains(item.strMeal)){
            LikedDatabaseDemo.likedList.remove(item.strMeal)
        }else{
            LikedDatabaseDemo.likedList.add(item.strMeal)
        }
        updateItem.value = position
    }

    fun getNavigationBundle(item: Meal) : Bundle {
        val bundle = Bundle()
        bundle.putString("ITEM_ID", item.idMeal)
        return bundle
    }

}