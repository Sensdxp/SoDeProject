package com.example.sodeproject.feature_scanner.presentation

data class ScannerState (
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
)
