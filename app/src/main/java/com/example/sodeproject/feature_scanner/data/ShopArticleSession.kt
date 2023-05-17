package com.example.sodeproject.feature_scanner.data

object ShopArticleSession {
    var articleList: List<ShopArticle> = emptyList()


    var addPoints: Int = 0
    var shopId: String= ""
    var customerId: String = ""

    // Offer Inhalte
    var offerId: String = ""
    var offer: String = ""
    var offerCost: Int = 0

}
// Alles was Ein Artikel beinhaltet
data class ShopArticle(
    val articleId: String,
    val description: String,
    val scorePoints: Int
    )