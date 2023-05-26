package com.example.sodeproject.feature_shop.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_shop.data.ShopRepository
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShopInfoViewModel @Inject constructor(
    private val repository: ShopRepository
): ViewModel() {

    val _shopInfoState = Channel<ShopInfoState>()
    val shopState = _shopInfoState.receiveAsFlow()

    init {
        getArticle(ActiveInfoShop.shop.id)
    }

    fun getArticle(shopId: String) = viewModelScope.launch {
        repository.getArticle(shopId).collect { result ->
            when (result) {
                is Resource.Success -> {
                    _shopInfoState.send(ShopInfoState(isSuccess = true))
                }

                is Resource.Loading -> {
                    _shopInfoState.send(ShopInfoState(isLoading = true))
                }

                is Resource.Error -> {
                    _shopInfoState.send(ShopInfoState(isError = true))
                }
            }
        }
    }
}
