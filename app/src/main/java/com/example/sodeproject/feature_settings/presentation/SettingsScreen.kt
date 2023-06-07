package com.example.sodeproject.feature_settings.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenSuperLight

@Composable
fun SettingsScreen(navController: NavController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .drawBehind {
                val path = Path()
                val x = size.width
                val y = size.height
                val center = size.center * 6F / 8F
                path.apply {
                    moveTo(0f, 0f)
                    lineTo(x, 0f)
                    lineTo(x, center.y * 1 / 4)
                    cubicTo(
                        x1 = x,
                        y1 = center.y * 1 / 2,
                        x2 = x,
                        y2 = center.y,
                        x3 = 0f,
                        y3 = center.y
                    )
                }
                drawPath(path = path, color = GreenLight)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(vertical = 48.dp, horizontal = 36.dp).fillMaxHeight(),
            verticalArrangement = Arrangement.Top
        ) {
            Text(text = "Settings", color = Color.White, fontSize = 70.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Current Version: 1.0", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "SmartBuy", color = Color.White, fontSize = 30.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(height = 96.dp))

            Text(text = "User data:",fontSize = 20.sp)

            Spacer(modifier = Modifier.height(height = 16.dp))

            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.Top
            ) {
                Text(text = "Username: ${UserSession.userName}",fontSize = 20.sp)

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Email: ${UserSession.userMail}",fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    navController.navigate("SignIn_Screen")
                    UserSession.userMail = ""
                    UserSession.uid = ""
                },
                modifier =
                Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenLight,
                    contentColor = Color.White
                ),
            ) {
                Text(text = "Logout")
            }
        }
        BottomNavigationBar(navController = navController)
    }
}