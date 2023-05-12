package com.example.sodeproject.feature_navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sodeproject.feature_login.presentation.login_screen.SignInScreen
import com.example.sodeproject.feature_login.presentation.signup_screen.SignUpScreen
import com.example.sodeproject.feature_scanner.presentation.ScannerScreen
import com.example.sodeproject.feature_score.presentation.ScoreScreen
import com.example.sodeproject.feature_settings.presentation.SettingsScreen
import com.example.sodeproject.feature_shop.presentation.ShopScreen

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SignInScreen.route,
    ){
        composable(route = Screens.SignInScreen.route){
            SignInScreen(navController)
        }
        composable(route = Screens.SignUpScreen.route){
            SignUpScreen(navController)
        }
        composable(route = Screens.ScoreScreen.route){
            ScoreScreen(navController)
        }
        composable(route = Screens.ScannerScreen.route){
            ScannerScreen(navController)
        }
        composable(route = Screens.ShopScreen.route){
            ShopScreen(navController)
        }
        composable(route = Screens.SettingsScreen.route){
            SettingsScreen(navController)
        }
    }
}




