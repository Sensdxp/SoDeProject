package com.example.sodeproject.util

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)
}


fun calculateSizeFactor(width: Dp): Float {
    val phoneWidth = 397.dp

    val scaleFactor = width / phoneWidth

    return scaleFactor.toFloat()
}

fun calculateHightFactor(height: Dp): Float {
    val phoneheight = 713.dp

    val scaleFactor = height / phoneheight

    return scaleFactor.toFloat()
}

