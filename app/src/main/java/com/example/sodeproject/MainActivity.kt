package com.example.sodeproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.rememberNavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_login.presentation.login_screen.SignInViewModel
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_navigation.NavigationGraph
import com.example.sodeproject.feature_navigation.components.BottomNavItem


import com.example.sodeproject.ui.theme.SoDeProjectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    //@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoDeProjectTheme {

                val navController = rememberNavController()
                NavigationGraph(navController = navController)
                /*
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
                                    icon = Icons.Default.Check
                                ),
                                BottomNavItem(
                                    name = "Score",
                                    route = "Score_Screen",
                                    icon = Icons.Default.Star
                                ),
                                BottomNavItem(
                                    name = "Settings",
                                    route = "Settings_Screen",
                                    icon = Icons.Default.ShoppingCart
                                )
                            ),
                            navController = navController,
                            onItemClick = {
                                navController.navigate(it.route)
                            }
                        )
                    }
                ) { innerPadding ->
                    Box(Modifier.padding(innerPadding)) {
                        NavigationGraph(navController = navController)
                    }
                }

                 */
            }
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