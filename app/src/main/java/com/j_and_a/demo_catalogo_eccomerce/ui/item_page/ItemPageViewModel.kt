package com.j_and_a.demo_catalogo_eccomerce.ui.item_page

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.j_and_a.demo_catalogo_eccomerce.api.RetrofitInstance
import com.j_and_a.demo_catalogo_eccomerce.database.LikedDatabaseDemo
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.model.MealSearchGson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemPageViewModel : ViewModel() {

    val currentItem = MutableLiveData<Meal?>()
    val updateView = MutableLiveData<Boolean>().apply { value = false }

    fun fetchItemDetails(itemId: String){
        RetrofitInstance.api.getMealById(itemId).enqueue(object: Callback<MealSearchGson>{
            override fun onResponse(p0: Call<MealSearchGson>, p1: Response<MealSearchGson>) {
                p1.body().let { body ->
                    currentItem.postValue(body!!.meals[0])
                }
            }
            override fun onFailure(p0: Call<MealSearchGson>, p1: Throwable) {
                Log.d("ItemPageViewModel","Error while fetching item")
            }
        })
    }

    /**
     *  Like database update logic
     *  This should be moved on the database logic file
     **/

    fun updateLike(){
        if(LikedDatabaseDemo.likedList.contains(currentItem.value?.strMeal)){
            LikedDatabaseDemo.likedList.remove(currentItem.value?.strMeal)
        }else{
            LikedDatabaseDemo.likedList.add(currentItem.value!!.strMeal)
        }
        updateView.value = true
    }
}