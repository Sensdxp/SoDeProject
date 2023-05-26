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
                val x = size.width
                val y = size.height
                val center = size.center * 5F / 8F
                path.apply {
                    moveTo(0f, 0f)
                    lineTo(x, 0f)
                    lineTo(x, center.y)
                    cubicTo(
                        x1 = x * 3 / 4,
                        y1 = center.y * 7 / 6,
                        x2 = x * 1 / 4,
                        y2 = center.y * 7 / 6,
                        x3 = 0f,
                        y3 = center.y
                    )
                }
                val gradientShader = LinearGradientShader(
                    from = Offset.Zero,
                    to = Offset(x, center.y),
                    colors = listOf(GreenSuperDark, GreenSuperLight)
                )
                val brush = ShaderBrush(shader = gradientShader)

                drawPath(path = path, brush = brush)
            },
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                .drawBehind {
                    val path = Path()
                    val x = size.center.x * 2
                    val y = size.center.y * 11/4
                    val center = size.center * 6F / 8F
                    path.apply {
                        moveTo(x * 1 / 8, y * 5 / 16)
                        lineTo(x * 1 / 8, y * 3 / 16)
                        cubicTo(
                            x1 = x * 1 / 8,
                            y1 = y * 1 / 8,
                            x2 = x * 2 / 8,
                            y2 = y * 1 / 8,
                            x3 = x * 3 / 8,
                            y3 = y * 1 / 8
                        )
                        lineTo(x * 5 / 8, y * 1 / 8)
                        cubicTo(
                            x1 = x * 6 / 8,
                            y1 = y * 1 / 8,
                            x2 = x * 7 / 8,
                            y2 = y * 1 / 8,
                            x3 = x * 7 / 8,
                            y3 = y * 3 / 16
                        )
                        lineTo(x * 7 / 8, y * 10 / 32)
                        cubicTo(
                            x1 = x * 7 / 8,
                            y1 = y * 10 / 32,
                            x2 = x * 7 / 8,
                            y2 = y * 11 / 32,
                            x3 = x * 6 / 8,
                            y3 = y * 11 / 32
                        )
                        lineTo(x * 2 / 8, y * 11 / 32)
                        cubicTo(
                            x1 = x * 1 / 8,
                            y1 = y * 11 / 32,
                            x2 = x * 1 / 8,
                            y2 = y * 10 / 32,
                            x3 = x * 1 / 8,
                            y3 = y * 10 / 32
                        )
                    }

                    drawPath(path = path, color = androidx.compose.ui.graphics.Color.White)
                },
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier= Modifier
                    .fillMaxHeight()
                    .padding(vertical = 150.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = base64ToImageBitmap(imageBase64 = "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"),
                    contentDescription = "Bildbeschreibung",
                    modifier = Modifier.size(170.dp, 170.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row() {
                    androidx.compose.material3.Button(
                        onClick = { /* Implement logout logic here */ },
                        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                            containerColor = GreenSuperDark,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        ),
                    ) {
                        Text(text = "Open Offer", Modifier.padding(horizontal = 20.dp))
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(
                    modifier= Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Who is ${"Aldi"}?", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenSuperDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ActiveInfoShop.shop.shopDescription", color = GreenMain)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Offer:", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenSuperDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "ActiveInfoShop.shop.offer", color = GreenMain)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Price: ${200} Points", color = GreenMain)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "Products:", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = GreenSuperDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Schuhe:            20P", color = GreenMain)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SoDeProjectTheme {
        Greeting("Android")
    }
}


