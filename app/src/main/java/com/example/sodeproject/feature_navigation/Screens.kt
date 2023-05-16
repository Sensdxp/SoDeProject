package com.example.sodeproject.feature_navigation

sealed class Screens(val route: String) {
    object SignInScreen: Screens(route = "SignIn_Screen")
    object SignUpScreen: Screens(route = "SignUp_Screen")
    object SettingsScreen: Screens(route = "Settings_Screen")
    object ScannerScreen: Screens(route = "Scanner_Screen")
        object QRScannerScreen: Screens(route = "QRScanner_Screen")
            object OfferScreen: Screens(route = "Offer_Screen")
    object ScoreScreen: Screens(route = "Score_Screen")
    object ShopScreen: Screens(route = "Shop_Screen")
        object ShopInfoScreen: Screens(route = "ShopInfo_Screen")
}