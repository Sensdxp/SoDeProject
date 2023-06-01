package com.example.sodeproject.feature_shop.data

object ShopSession {
    var shoplist: List<Shop> = emptyList()
    var challangelist: List<Challenges> = emptyList()
}
data class Shop(
    var id: String = "",
    var logo: String = "",
    var name: String = "",
    var offer: String = "",
    var offerId: String = "",
    var offerCost: Int = 0,
    var shopDescription: String = ""
)

data class Challenges(
    var description: String = "empty",
    var reward: Int = 0,
    var done: Boolean = false,
    var goal: Int = 100,
    var progress: Int = 0
)