package com.example.sodeproject.feature_scanner.data

import com.example.sodeproject.util.Resource
import kotlinx.coroutines.flow.Flow

interface ScannerRepository {
    fun getArticles(userId: String): Flow<Resource<Int>>
}
/*
interface ShopRepository {
    fun getShop(): Flow<Resource<Int>>
}
 */