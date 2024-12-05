package com.j_and_a.demo_catalogo_eccomerce.model

/**
 *  Data class for the Category
 **/

data class CategoryListGson(val categories: List<CategoryItem>)

data class CategoryItem(val idCategory: String,
                        val strCategory : String,
                        val strCategoryThumb: String,
                        val strCategoryDescription : String)