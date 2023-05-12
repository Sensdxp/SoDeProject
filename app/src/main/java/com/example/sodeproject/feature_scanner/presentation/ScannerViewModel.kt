package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ScannerViewModel: ViewModel() {
    val code = mutableStateOf("")
    var hasCameraPermission = mutableStateOf(false)
}