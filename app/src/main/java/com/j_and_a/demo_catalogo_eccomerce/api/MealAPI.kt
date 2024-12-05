package com.j_and_a.demo_catalogo_eccomerce.api

import com.j_and_a.demo_catalogo_eccomerce.model.CategoryListGson
import com.j_and_a.demo_catalogo_eccomerce.model.MealSearchGson
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MealAPI {
    @GET("search.php")
    fun searchMeal(
        @Query("s") search : String
    ) : Call<MealSearchGson>

    @GET("lookup.php")
    fun getMealById(
        @Query("i") id : String
    ) : Call<MealSearchGson>

    @GET("categories.php")
    fun getCategories(
    ) : Call<CategoryListGson>

    @GET("filter.php")
    fun getAllMealsByCategory(
        @Query("c") category : String
    ) : Call<MealSearchGson>

    @GET("random.php")
    fun getRandomMeal(
    ): Call<MealSearchGson>
}