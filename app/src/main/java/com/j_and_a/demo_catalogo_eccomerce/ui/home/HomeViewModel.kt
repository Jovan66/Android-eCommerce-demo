package com.j_and_a.demo_catalogo_eccomerce.ui.home

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.j_and_a.demo_catalogo_eccomerce.api.RetrofitInstance
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.model.CategoryItem
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.model.MealSearchGson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
private const val MAX_ELEMENTS = 5

class HomeViewModel : ViewModel() {

    val testDataset = ArrayList<Meal>()

    val updateView = MutableLiveData<Boolean>().apply { value = false }
    val updateItem = MutableLiveData<Int>().apply { value = -1 }

    /**
     *  Random items selected for the recyclerViews in homepage
     *  Can be done in less code but I can't be bordered
     **/
    fun prepareDataSet(){
        for (i in 0..5){
            fetchRandomItem(i)
        }
    }

    private fun fetchRandomItem(counter: Int){
        if (testDataset.size - 1 >= MAX_ELEMENTS)
            return

        RetrofitInstance.api.getRandomMeal().enqueue(object: Callback<MealSearchGson> {
            override fun onResponse(p0: Call<MealSearchGson>, p1: Response<MealSearchGson>) {
                p1.body().let { meal ->
                    testDataset.add(meal!!.meals[0])
                    if (counter ==  5 ) updateView.value = true
                }
            }
            override fun onFailure(p0: Call<MealSearchGson>, p1: Throwable) {
                Log.d("HomeViewModel","Error while fetching Random Items in Homepage")
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

    /**
     *  Setting the data in the bundle to correctly lode the next fragment
     **/
    fun getNavigationBundle(item: Meal) : Bundle {
        val bundle = Bundle()
        bundle.putString("ITEM_ID", item.idMeal)
        return bundle
    }
}