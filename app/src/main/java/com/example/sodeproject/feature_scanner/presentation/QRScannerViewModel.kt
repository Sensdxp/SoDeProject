package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class QRScannerViewModel: ViewModel() {
    var hasCameraPermission = mutableStateOf(false)
    val code = mutableStateOf("")
}