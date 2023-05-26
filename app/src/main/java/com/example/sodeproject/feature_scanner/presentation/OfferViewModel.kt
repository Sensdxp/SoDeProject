package com.example.sodeproject.feature_scanner.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_scanner.data.ScannerRepository
import com.example.sodeproject.feature_scanner.data.ShopArticleSession
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfferViewModel @Inject constructor(
    private val repository: ScannerRepository
): ViewModel() {
    val _offerState = Channel<OfferState>()
    val offerState = _offerState.receiveAsFlow()

    init {
        checkOfferScore(ShopArticleSession.customerId,ShopArticleSession.addPoints,ShopArticleSession.shopId,ShopArticleSession.offerId)
    }
    /*
    fun getOffer(userId: String, offerId: String) = viewModelScope.launch {
        repository.getOffer(userId, offerId).collect() { result ->
            when (result) {
                is Resource.Success -> {
                    _offerState.send(OfferState(isSuccess = true))
                }

                is Resource.Loading -> {
                    _offerState.send(OfferState(isLoading = true))
                }

                is Resource.Error -> {
                    _offerState.send(OfferState(isError = true))
                }
            }
        }
    }

     */

    fun checkOfferScore(customerId: String, addScore: Int, shopId: String, offerId: String) = viewModelScope.launch {
        repository.checkOfferScore(customerId,addScore,shopId,offerId).collect() { result ->
            when (result) {
                is Resource.Success -> {
                    _offerState.send(OfferState(isSuccess = true))
                }

                is Resource.Loading -> {
                    _offerState.send(OfferState(isLoading = true))
                }

                is Resource.Error -> {
                    _offerState.send(OfferState(isError = true))
                }
            }
        }
    }

    fun updateScore(addScore: Int, userId: String,shopId: String) = viewModelScope.launch {
        repository.updateScore(addScore,userId, shopId).collect(){result ->
            when (result) {
                is Resource.Success -> {

                }
                is Resource.Loading -> {

                }
                is Resource.Error -> {

                }
            }
        }
    }
}