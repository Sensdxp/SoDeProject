package com.example.sodeproject.feature_scanner.data

object ShopArticleSession {
    var articleList: List<ShopArticle> = emptyList()
    var addPoints: Int = 0
    var offer: String = ""
    var offerId: String = ""
}

data class ShopArticle(
    val articleId: String,
    val description: String,
    val scorePoints: Int
    )