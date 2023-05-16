package com.example.sodeproject.feature_score.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar

@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel()
    ) {
    val scoreState = viewModel.scoreState.collectAsState(initial = null)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(UserSession.seller == false || UserSession.seller == null) {
            Column() {
                if (scoreState.value?.isLoading == true) {
                    CircularProgressIndicator()
                } else {
                    if (scoreState.value?.isError == true) {
                        Text(text = "Error downloading score: ${scoreState.value?.isError}")
                    }else{
                        Text(text = "Your score is: ${UserSession.score}")
                    }
                }

            }
        }else{
            Text(text = "shop guy")
        }
        
        BottomNavigationBar(navController = navController)
    }
}