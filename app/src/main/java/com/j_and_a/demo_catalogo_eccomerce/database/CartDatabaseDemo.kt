package com.j_and_a.demo_catalogo_eccomerce.database

/**
 *  Fake local database for items in the cart
 **/

class CartDatabaseDemo {
        companion object {
        var cartList : ArrayList<String> = ArrayList()
        fun setAll(){
            cartList.add("Banana Pancakes")
            cartList.add("BBQ Pork Sloppy Joes")
            cartList.add("Japanese Katsudon")
        }

        fun addLiked(id: String){ cartList.add(id) }

        fun removeLiked(id: String){ cartList.remove(id) }

    }
}