package com.example.sodeproject.feature_login.data

import com.example.sodeproject.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun loginUser(email:String, password:String): Flow<Resource<AuthResult>>
    fun registerUser(userName: String, email: String, password: String): Flow<Resource<AuthResult>>
}