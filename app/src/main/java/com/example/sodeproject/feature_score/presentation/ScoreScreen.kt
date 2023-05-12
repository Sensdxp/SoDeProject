package com.example.sodeproject.feature_score.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar

/*
@Composable
fun ScoreScreen1(viewModel: ScoreViewModel = viewModel()) {
    val dataState = viewModel.response.value
    val score = (dataState as? DataState.Success)?.data?.score ?: 0

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (dataState) {
            is DataState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is DataState.Failure -> {
                Text(dataState.message)
            }
            is DataState.Empty -> {
                Text(
                    text = "No data available",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is DataState.Success -> {
                Text(
                    text = score.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun ScoreScreen(userId: String, viewModel: ScoreViewModel = viewModel()) {
    val dataState by viewModel.response
    (if(UserSession.uid != null){UserSession.uid} else "User ???")?.let { Text(text = it) }
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (dataState) {
            is DataState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is DataState.Failure -> {
                Text((dataState as DataState.Failure).message)
            }
            is DataState.Empty -> {
                Text(
                    text = "No data available",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            is DataState.Success -> {
                val score = (dataState as DataState.Success).data.score
                Text(
                    text = score.toString(),
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

 */

@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel()
    ) {
    val scoreState = viewModel.scoreState.collectAsState(initial = null)
    val score = scoreState.value
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(UserSession.seller == false || UserSession.seller == null) {
            Column() {
                if (scoreState.value?.isLoading == true) {
                    CircularProgressIndicator()
                } else {
                    if (scoreState.value?.isSuccess == true) {
                        Text(text = "Your score is: ${UserSession.score}")
                    }
                    if (scoreState.value?.isError == true) {
                        Text(text = "Error downloading score: ${scoreState.value?.isError}")
                    }
                }

            }
        }else{
            Text(text = "shop guy")
        }
        
        BottomNavigationBar(navController = navController)
    }
}