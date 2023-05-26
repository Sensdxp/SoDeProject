package com.example.sodeproject.feature_login.presentation.login_screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.R
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.ui.theme.GreenSuperLight
import com.example.sodeproject.ui.theme.LightBlue
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen (
    navController: NavController,
    viewModel: SignInViewModel = hiltViewModel()
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val state = viewModel.signInState.collectAsState(initial = null)

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    val scaleFactor = if (screenWidth < screenHeight) {
        screenWidth / 360.dp // Baseline Breite für Skalierung
    } else {
        screenHeight / 640.dp // Baseline Höhe für Skalierung
    }


    Box(modifier = Modifier
        .fillMaxSize()
        .background(GreenSuperLight)
        .drawBehind {
            val path = Path()
            val x = size.width
            val y = size.height
            val center = size.center * 6F / 8F
            path.apply {
                moveTo(0f, 0f)
                lineTo(x, 0f)
                lineTo(x, center.y * 3 / 4)
                cubicTo(
                    x1 = x,
                    y1 = center.y * 15 / 16,
                    x2 = x,
                    y2 = center.y,
                    x3 = center.x * 15 / 16,
                    y3 = center.y
                )
                //lineTo(center.x * 1/4, center.y)
                cubicTo(
                    x1 = 0f,
                    y1 = center.y,
                    x2 = 0f,
                    y2 = center.y * 16 / 15,
                    x3 = 0f,
                    y3 = center.y * 5 / 4
                )
            }
            drawPath(path = path, color = Color.White)
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 30.dp, end = 30.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.mipmap.img),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(width = screenHeight*5/16, height = screenHeight*5/16)
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
            )

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 36.sp * scaleFactor
                )

            }

            Text(
                text = "Enter your personal data to login",
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = Color.White,
                fontFamily = FontFamily.Default
            )

            TextField(
                leadingIcon = { Icon(tint = GreenSuperDark,imageVector = Icons.Default.Email, contentDescription = "emailIcon") },
                value = email,
                onValueChange = { email = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    disabledLabelColor = LightBlue,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    containerColor = GreenSuperLight,
                    textColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "Email", color = GreenSuperDark)
                }
            )
            Divider(
                color = GreenSuperDark,
                thickness = 5.dp)
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                leadingIcon = { Icon(tint = GreenSuperDark,imageVector = Icons.Default.Lock, contentDescription = "emailIcon") },
                value = password,
                onValueChange = { password = it },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color.Black,
                    disabledLabelColor = LightBlue,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    containerColor = GreenSuperLight,
                    textColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp),
                singleLine = true,
                placeholder = {
                    Text(text = "Password", color = GreenSuperDark)
                }
            )
            Divider(
                color = GreenSuperDark,
                thickness = 5.dp)

            Button(
                onClick = {
                    scope.launch {
                        viewModel.loginUser(email, password)
                    }
                },
                modifier = Modifier
                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenSuperDark,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    text = "Sign In",
                    color = Color.White,
                    modifier = Modifier.padding(7.dp),
                    fontSize = 15.sp * scaleFactor
                )
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                if (state.value?.isLoading == true) {
                    CircularProgressIndicator(color = Color.White)
                }
            }

            ClickableText(
                text = AnnotatedString("Do not have an Account? Sign Up"),
                onClick = { navController.navigate("SignUp_Screen") },
                style = TextStyle(
                    color = Color.White
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.Center
            ) {

                LaunchedEffect(key1 = state.value?.isSuccess) {
                    scope.launch {
                        if (state.value?.isSuccess?.isNotEmpty() == true) {
                            navController.navigate("Score_Screen")
                            val success = state.value?.isSuccess
                            Toast.makeText(context, "${success}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
                LaunchedEffect(key1 = state.value?.isError) {
                    scope.launch {
                        if (state.value?.isError?.isNotEmpty() == true) {
                            val error = state.value?.isError
                            Toast.makeText(context, "${error}", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}