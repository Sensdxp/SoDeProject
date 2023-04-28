package com.example.sodeproject.feature_navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sodeproject.feature_login.presentation.login_screen.SignInScreen
import com.example.sodeproject.feature_login.presentation.signup_screen.SignUpScreen
import com.example.sodeproject.feature_scanner.presentation.ScannerScreen
import com.example.sodeproject.feature_score.presentation.ScoreScreen
import com.example.sodeproject.feature_settings.presentation.SettingsScreen
import com.example.sodeproject.feature_shop.presentation.ShopScreen
/*
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "SignIn_Screen") {
        composable("SignIn_Screen"){
            SignInScreen(navController)
        }
        composable("SignUp_Screen"){
            SignUpScreen(navController)
        }
        composable("Score_Screen"){
            ScoreScreen()
        }
        composable("Scanner_Screen"){
            ScannerScreen()
        }
        composable("Shop_Screen"){
            ShopScreen()
        }
        composable("Settings_Screen"){
            SettingsScreen()
        }
    }
}
*/
@Composable
fun NavigationGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = Screens.SignUpScreen.route
    ){
        composable(route = Screens.SignInScreen.route){
            SignInScreen(navController)
        }
        composable(route = Screens.SignUpScreen.route){
            SignUpScreen(navController)
        }
        composable(route = Screens.ScoreScreen.route){
            ScoreScreen()
        }
        composable(route = Screens.ScannerScreen.route){
            ScannerScreen()
        }
        composable(route = Screens.ShopScreen.route){
            ShopScreen()
        }
        composable(route = Screens.SettingsScreen.route){
            SettingsScreen()
        }
    }
}



