package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.runtime.mutableStateOf
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
class ScannerViewModel @Inject constructor(
    private val repository: ScannerRepository
): ViewModel() {
    val code = mutableStateOf("")
    var hasCameraPermission = mutableStateOf(false)

    val _scannerState = Channel<ScannerState>()
    val scannerState = _scannerState.receiveAsFlow()

    init {
        ShopArticleSession.addPoints = 0
        if(UserSession.seller == true) {
            UserSession.uid?.let { getArticles(it) }
        }
    }

    fun getArticles(userId: String) = viewModelScope.launch {
        repository.getArticles(userId).collect(){result ->
            when (result) {
                is Resource.Success -> {
                    _scannerState.send(ScannerState(isSuccess = true))
                }
                is Resource.Loading -> {
                    _scannerState.send(ScannerState(isLoading = true))
                }
                is Resource.Error -> {
                    _scannerState.send(ScannerState(isError = true))
                }
            }
        }
    }
}