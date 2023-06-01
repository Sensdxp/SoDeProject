package com.example.sodeproject.feature_shop.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_shop.data.ShopRepository
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShopViewModel @Inject constructor(
    private val repository: ShopRepository
): ViewModel() {

    val _shopState = Channel<ShopState>()
    val shopState = _shopState.receiveAsFlow()

    init {
        if(UserSession.seller == false) {
            getShops()
        }else{
            UserSession.uid?.let { getShopInst(it) }
        }
    }

    fun getShops() = viewModelScope.launch {
        repository.getShop().collect { result ->
            when (result) {
                is Resource.Success -> {
                    _shopState.send(ShopState(isSuccess = true))
                }

                is Resource.Loading -> {
                    _shopState.send(ShopState(isLoading = true))
                }

                is Resource.Error -> {
                    _shopState.send(ShopState(isError = true))
                }
            }
        }
    }

    fun getShopInst(shopId: String) = viewModelScope.launch {
        repository.getShopInst(shopId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _shopState.send(ShopState(isSuccess = true))
                }

                is Resource.Loading -> {
                    _shopState.send(ShopState(isLoading = true))
                }

                is Resource.Error -> {
                    _shopState.send(ShopState(isError = true))
                }
            }
        }
    }
}