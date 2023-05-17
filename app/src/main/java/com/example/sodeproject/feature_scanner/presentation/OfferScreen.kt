package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_scanner.data.ShopArticleSession

@Composable
fun OfferScreen(
    navController: NavController,
    offerViewModel: OfferViewModel = hiltViewModel()
) {
    val offerState = offerViewModel.offerState.collectAsState(initial = null)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if(offerState.value?.isLoading == true){
            CircularProgressIndicator()
        } else {
            if(offerState.value?.isError == true){
                Column() {
                    Text(text = "Customer has used an invalid code.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            navController.navigate("QRScanner_Screen")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Try another one")
                    }
                }
            }else{
                Column() {
                    Text(text = ShopArticleSession.offer)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            var addScore = ShopArticleSession.addPoints - ShopArticleSession.offerCost
                            offerViewModel.updateScore(addScore, ShopArticleSession.customerId)
                            navController.navigate("Scanner_Screen")
                            ShopArticleSession.customerId = ""
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text(text = "Confirm")
                    }
                }
            }
        }
    }
    BottomNavigationBar(navController = navController)
}