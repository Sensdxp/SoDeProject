package com.example.sodeproject

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sodeproject.feature_navigation.NavigationGraph


import com.example.sodeproject.ui.theme.SoDeProjectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    //private val viewModel: SignInViewModel by viewModels()

    //@OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NavigationGraph()
            /*
            val navController = rememberNavController()
            var context1 = LocalContext.current

            if(viewModel.getSignInSuccess()){
                Scaffold(
                    bottomBar = {
                        BottomNavigationBar(
                            items = listOf(
                                BottomNavItem(
                                    name = "Shop",
                                    route = "Shop_Screen",
                                    icon = Icons.Default.Settings
                                ),
                                BottomNavItem(
                                    name = "Scanner",
                                    route = "Scanner_Screen",
                                    icon = Icons.Default.Settings
                                ),
                                BottomNavItem(
                                    name = "Score",
                                    route = "Score_Screen",
                                    icon = Icons.Default.Settings
                                ),
                                BottomNavItem(
                                    name = "Settings",
                                    route = "Settings_Screen",
                                    icon = Icons.Default.Settings
                                )
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) {
                    Navigation(navController = navController)
                }
            }
            */
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoDeProjectTheme {
        Greeting("Android")
    }
}