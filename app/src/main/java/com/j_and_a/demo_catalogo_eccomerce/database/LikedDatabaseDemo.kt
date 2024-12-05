package com.j_and_a.demo_catalogo_eccomerce.database

/**
 *  Fake database for items in the wish list
 **/

class LikedDatabaseDemo {

    companion object {
        var likedList : ArrayList<String> = ArrayList()
        fun setAll(){
            likedList.add("Banana Pancakes")
            likedList.add("Chocolate Raspberry Brownies")
            likedList.add("BBQ Pork Sloppy Joes")
            likedList.add("Japanese Katsudon")
            likedList.add("Blini Pancakes")
            likedList.add("Split Pea Soup")
            likedList.add("Spaghetti alla Carbonara")
            likedList.add("Big Mac")
        }

        fun addLiked(id: String){ likedList.add(id) }

        fun removeLiked(id: String){ likedList.remove(id) }

    }
}