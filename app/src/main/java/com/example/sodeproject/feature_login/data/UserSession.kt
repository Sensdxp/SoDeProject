package com.example.sodeproject.feature_login.data

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.sodeproject.feature_scanner.data.Trans


object UserSession {
    var userName: String = ""
    var uid: String? = null
    var score: Int? = null
    var seller: Boolean? = null
    var trans: List<Trans> = emptyList()
    var userMail: String= ""
}