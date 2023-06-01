package com.example.sodeproject.feature_scanner.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_scanner.data.ShopArticleSession
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.util.calculateHightFactor

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
        val screenWidth = LocalConfiguration.current.screenHeightDp.dp
        val wfac = calculateHightFactor(screenWidth)
        if(offerState.value?.isLoading == true){
            CircularProgressIndicator()
        } else {
            if(offerState.value?.isError == true){
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Yellow Warning",
                        tint = Color.Red,
                        modifier = Modifier.size(300.dp * wfac)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "Customer has used an invalid code.",fontSize =25.sp * wfac, color = GreenLight)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            navController.navigate("QRScanner_Screen")
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GreenLight,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Try another one", fontSize = 25.sp * wfac, color =  androidx.compose.ui.graphics.Color.White)
                    }
                }
            }else{
                Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Green Check",
                        tint = GreenLight,
                        modifier = Modifier.size(300.dp * wfac)
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(text = "User gets: ${ShopArticleSession.offer}", fontSize = 25.sp * wfac, color = GreenLight)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = {
                            var addScore = ShopArticleSession.addPoints - ShopArticleSession.offerCost
                            UserSession.uid?.let {
                                offerViewModel.updateScore(addScore, ShopArticleSession.customerId,
                                    it
                                )
                            }
                            navController.navigate("Scanner_Screen")
                            ShopArticleSession.customerId = ""
                        },
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = GreenLight,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(text = "Confirm", fontSize = 25.sp * wfac, color =  androidx.compose.ui.graphics.Color.White)
                    }
                }
            }
        }
    }
    BottomNavigationBar(navController = navController)
}