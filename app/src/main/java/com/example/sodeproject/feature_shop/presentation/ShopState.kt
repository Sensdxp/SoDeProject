package com.example.sodeproject.feature_shop.presentation

import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_shop.data.Shop

data class ShopState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
)

object ActiveInfoShop {
    var shop: Shop = Shop("", "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", "", "", "", 0,"")
    var articleList: List<ShopArticle> = emptyList()
}