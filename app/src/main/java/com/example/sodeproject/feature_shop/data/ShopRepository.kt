package com.example.sodeproject.feature_shop.data

import com.example.sodeproject.util.Resource
import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    fun getShop(): Flow<Resource<Int>>

    fun getShopInst(shopId: String): Flow<Resource<Int>>
    fun getArticle(shopId: String) :Flow<Resource<Int>>

}