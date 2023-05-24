package com.example.sodeproject.feature_score.data

import androidx.compose.runtime.mutableStateListOf

data class User(
    val userId:String  = "",
    val score:Int = 0
)

object ChartSession {
    var mCustomer: List<Float> = listOf(0f,0f,0f,0f,0f)
    var mScore: List<Float> = listOf(0f,0f,0f,0f,0f)
    var mOffer: List<Float> = listOf(0f,0f,0f,0f,0f)
    var totalScore: Int = 0
}
