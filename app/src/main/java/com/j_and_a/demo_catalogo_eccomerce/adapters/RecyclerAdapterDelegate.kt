package com.j_and_a.demo_catalogo_eccomerce.adapters

/**
 * Delegate for interactions with elements in adapters
 **/

interface RecyclerAdapterDelegate {
    fun recyclerViewFunction(
        selectedData: Any,
        tag: String,
        position: Int
    )
}