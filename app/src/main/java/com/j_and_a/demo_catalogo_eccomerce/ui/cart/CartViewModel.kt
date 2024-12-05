package com.j_and_a.demo_catalogo_eccomerce.ui.cart

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.j_and_a.demo_catalogo_eccomerce.api.RetrofitInstance
import com.j_and_a.demo_catalogo_eccomerce.model.Meal
import com.j_and_a.demo_catalogo_eccomerce.model.MealSearchGson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartViewModel : ViewModel() {

    val _listMeals = MutableLiveData<ArrayList<Meal>>().apply { value = ArrayList<Meal>() }

    /**
     *  Fetching of the meals in the cart from database
     *  and updating the view
     **/

    fun fetchMeals(meals: ArrayList<String>){
        val mealsList = ArrayList<Meal>()
        meals.forEachIndexed{index,mealName->
            RetrofitInstance.api.searchMeal(mealName).enqueue(object:
                Callback<MealSearchGson> {
                override fun onResponse(p0: Call<MealSearchGson>, p1: Response<MealSearchGson>) {
                    p1.body().let { body ->
                        mealsList.add(body!!.meals[0])
                        if (index == meals.size - 1){ _listMeals.postValue(mealsList) }
                    }
                }
                override fun onFailure(p0: Call<MealSearchGson>, p1: Throwable) {
                    Log.d("CartViewModel",p1.message.toString())
                    Log.d("CartViewModel","Error while fetching Meals in CartFragment")
                }
            })
        }
    }
}