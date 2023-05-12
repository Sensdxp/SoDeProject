package com.example.sodeproject.feature_score.util

import com.example.sodeproject.feature_score.data.User

sealed class DataState{
    class Success(val data: User):DataState()
    class Failure(val message: String):DataState()
    object Loading:DataState()
    object Empty:DataState()
}
/*
 operator fun getValue(nothing: Nothing?, property: KProperty<*>): Any {

    }
 */
