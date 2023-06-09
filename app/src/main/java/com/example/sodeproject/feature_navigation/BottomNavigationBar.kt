package com.example.sodeproject.feature_navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp

import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.components.BottomNavItem
import com.example.sodeproject.ui.theme.GrayLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.util.calculateSizeFactor

@Composable
fun BottomNavigationBarItem(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick: (BottomNavItem) -> Unit
) {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
    val fac: Float = calculateSizeFactor(screenWidth)

    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 5.dp
    ) {
        items.forEach { item ->
            val selected = item.route == backStackEntry.value?.destination?.route
            BottomNavigationItem(
                selected = selected,
                onClick = { onItemClick(item) },
                selectedContentColor = GreenMain,
                unselectedContentColor = GrayLight,
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if(item.badgeCount > 0){
                            BadgedBox(badge = {
                                Text(text = item.badgeCount.toString())
                            }) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.name
                                )
                            }
                        }else{
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.name
                            )
                        }
                        if (selected){
                            Text(
                                text = item.name,
                                textAlign = TextAlign.Center,
                                fontSize = 10.sp * fac
                            )
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavController,
){
    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter) {
        BottomNavigationBarItem(items = listOf(
            BottomNavItem(
                name = "Shop",
                route = "Shop_Screen",
                icon = Icons.Default.ShoppingCart
            ),
            BottomNavItem(
                name = if(UserSession.seller == true) {"Scanner"} else "QR-code",
                route = "Scanner_Screen",
                icon = Icons.Default.Check
            ),
            BottomNavItem(
                name = if(UserSession.seller == true) {"Stats"} else "Score",
                route = "Score_Screen",
                icon = Icons.Default.Star
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
}