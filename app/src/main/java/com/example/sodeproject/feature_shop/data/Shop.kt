package com.example.sodeproject.feature_shop.data

object ShopSession{
    var shoplist: List<Shop> = emptyList()
}

data class Shop(
    val id: String = "",
    val logo: String = "",
    val name: String = "",
    val offer: String = "",
    val offerId: String = "",
    val offerCost: Int = 0,
    val shopDescription: String = ""
)