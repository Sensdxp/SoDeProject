package com.example.sodeproject.feature_score.presentation

data class StatsState (
    val isSuccess: Stats = Stats(
        mCustomer = listOf(0f,0f,0f,0f,0f),
        mScore = listOf(0f,0f,0f,0f,0f),
        mOffer = listOf(0f,0f,0f,0f,0f),
        totalScore = 0),
    val isLoading: Boolean = false,
    val isError: Boolean = false
)

data class Stats(
    val mCustomer: List<Float>,
    val mOffer: List<Float>,
    val mScore: List<Float>,
    val totalScore: Int
)