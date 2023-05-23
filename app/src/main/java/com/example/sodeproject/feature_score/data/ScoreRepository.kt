package com.example.sodeproject.feature_score.data

import com.example.sodeproject.util.Resource
import kotlinx.coroutines.flow.Flow

interface ScoreRepository {
    fun getScore(userId:String): Flow<Resource<Int>>
    fun saveScore(userId:String, score:Int): Flow<Resource<Int>>

    fun getChartData(userId: String): Flow<Resource<Int>>
}