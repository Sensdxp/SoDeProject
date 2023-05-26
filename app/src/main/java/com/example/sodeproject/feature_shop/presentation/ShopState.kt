package com.example.sodeproject.feature_shop.presentation

import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_shop.data.Shop

data class ShopState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
)

object ActiveInfoShop {
    var shop: Shop = Shop("", "", "", "", "", 0,"")
    var articleList: List<ShopArticle> = emptyList()
}