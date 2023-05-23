package com.example.sodeproject.feature_score.data

import androidx.compose.runtime.mutableStateListOf

data class User(
    val userId:String  = "",
    val score:Int = 0
)

object ChartSession {
    val mCustomer = mutableStateListOf<Float>()
    val mScore = mutableStateListOf<Float>()
    val mOffer = mutableStateListOf<Float>()
    var totalScore: Int = 0
}
