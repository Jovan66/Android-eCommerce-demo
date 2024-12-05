package com.j_and_a.demo_catalogo_eccomerce.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 *  Fix instance of the api called to invoke the api's calls
 **/

object RetrofitInstance {

    val api:MealAPI by lazy {
        Retrofit.Builder()
            .baseUrl("https://www.themealdb.com/api/json/v1/1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MealAPI::class.java)
    }
}