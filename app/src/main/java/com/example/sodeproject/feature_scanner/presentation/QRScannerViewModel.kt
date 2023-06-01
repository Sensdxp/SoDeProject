package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sodeproject.feature_scanner.data.ScannerRepository
import com.example.sodeproject.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.Scanner
import javax.inject.Inject

@HiltViewModel
class QRScannerViewModel @Inject constructor(
    private val repository: ScannerRepository
): ViewModel() {

    var hasCameraPermission = mutableStateOf(false)
    val code = mutableStateOf("")

    fun updateScore(addScore: Int, userId: String, shopId: String) = viewModelScope.launch {
        repository.updateScore(addScore,userId,shopId).collect(){result ->
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

    fun checkChallenges(addScore: Int, userId: String, shopId: String) = viewModelScope.launch {
        repository.checkChallenges(userId,addScore,shopId).collect(){result ->
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

    /*
    getArticles(userId: String) = viewModelScope.launch {
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
     */
}