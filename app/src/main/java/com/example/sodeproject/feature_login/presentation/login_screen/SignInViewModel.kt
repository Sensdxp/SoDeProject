package com.example.sodeproject.feature_login.presentation.login_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_login.data.AuthRepository
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: AuthRepository
): ViewModel() {

    //private var isSignInSuccess: Boolean = false

    val _signInState = Channel<SignInState>()
    val signInState = _signInState.receiveAsFlow()

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        repository.loginUser(email,password).collect { result ->
            when (result){
                is Resource.Success ->{
                    //isSignInSuccess = true
                    _signInState.send(SignInState(isSuccess = "Sign In Success "))
                }
                is Resource.Loading ->{
                    _signInState.send(SignInState(isLoading = true))
                }
                is Resource.Error ->{
                    _signInState.send(SignInState(isError = result.message))
                }
            }
        }
    }
    /*
    fun getSignInSuccess(): Boolean {
        return isSignInSuccess
    }
     */

}