package com.example.sodeproject


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.sodeproject.feature_navigation.NavigationGraph
import com.example.sodeproject.feature_shop.presentation.ActiveInfoShop
import com.example.sodeproject.feature_shop.presentation.ActiveInfoShop.shop
import com.example.sodeproject.feature_shop.presentation.base64ToImageBitmap
import com.example.sodeproject.ui.theme.GrayLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.ui.theme.GreenSuperLight


import com.example.sodeproject.ui.theme.SoDeProjectTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {



    //@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SoDeProjectTheme {

                val navController = rememberNavController()
                NavigationGraph(navController = navController)
            }
        }
    }




}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color(0xFFE9E9E9))
            .drawBehind {
                val path = Path()
                val x = size.center.x
                val y = size.center.y
                val center = size.center * 5F / 8F
                path.apply {
                    moveTo(0f, 0f)
                    lineTo(x*2, 0f)
                    lineTo(x*2, y + 100)
                    cubicTo(
                        x1 = x*2,
                        y1 = y - 50,
                        x2 = x*2 - 100,
                        y2 = y - 50,
                        x3 = x*2 -200,
                        y3 = y - 50
                    )
                    lineTo(200f, y - 50)
                    cubicTo(
                        x1 = 100f,
                        y1 = y - 50,
                        x2 = 0f,
                        y2 = y - 50,
                        x3 = 0f,
                        y3 = y +100
                    )
                }
                drawPath(path = path, color = Color.White)
            },
        contentAlignment = Alignment.Center
    ) {

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoDeProjectTheme {
        Greeting("Android")
    }
}



