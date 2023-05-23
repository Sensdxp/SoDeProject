package com.example.sodeproject.feature_score.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.sodeproject.R
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight


@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel()
    ) {
    val scoreState = viewModel.scoreState.collectAsState(initial = null)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(UserSession.seller == false || UserSession.seller == null) {
            Column() {
                if (scoreState.value?.isLoading == true) {
                    CircularProgressIndicator()
                } else {
                    if (scoreState.value?.isError == true) {
                        Text(text = "Error downloading score: ${scoreState.value?.isError}")
                    }else{
                        Text(text = "Your score is: ${UserSession.score}")
                    }
                }
            }
        }else{
            StoreScreen()
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun StoreScreen() {
    val selectedChart = remember { mutableStateOf("customer") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 50.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .background(GreenLight, shape = RoundedCornerShape(10.dp))
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        androidx.compose.material.Text(
                            text = "Hallo FixCoffee",
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Total Score Text
                        androidx.compose.material.Text(
                            text = "Your total score:",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Score value inside a circle
                ScoreLogo()
                Spacer(modifier = Modifier.height(16.dp))
                // Customer and Score fields
                CustomerScoreFields(selectedChart)
                // Bar Chart

                Box(modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(GreenLight)

                ) {
                    Column() {
                        Spacer(modifier = Modifier.height(32.dp))
                        BarChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            selectedChart = selectedChart.value,

                        )
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun BarChart(
    modifier: Modifier,
    barWidth: Dp = 22.dp,
    barSpacing: Dp = 10.dp,
    barColor: Color = Color.White,
    backgroundColor: Color = GreenLight,
    selectedChart: String,
    labelSize: Dp = 16.dp
) {
    val data = getChartData(selectedChart)
    val maxValue = getChartMax(selectedChart)
    val yListLabels = getyLabels(selectedChart)

    val maxDataValue = data.maxOrNull() ?: 0f
    val labelColor = Color.White

    val xLabels = listOf("Jan", "Feb", "Mar", "Apr", "May")
    val yLabels = yListLabels

    Canvas(modifier = modifier) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        val barCount = data.size
        val totalBarWidth = (barWidth + barSpacing).toPx() * barCount.toFloat() - barSpacing.toPx()

        val startX = ((canvasWidth - totalBarWidth) / 2)
        val startY = canvasHeight

        val xStep = totalBarWidth / (barCount - 1)
        val yStep = canvasHeight / 4

        drawRoundRect(
            color = backgroundColor,
            topLeft = Offset(startX, 0f),
            size = Size(totalBarWidth, canvasHeight),
            cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            style = Stroke(width = 2.dp.toPx())
        )

        data.forEachIndexed { index, value ->
            val barHeight = (value / maxValue) * canvasHeight

            val x = startX + (xStep * index)
            val y = startY - barHeight

            drawRoundRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = Size(barWidth.toPx(), barHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx()),
                style = Fill
            )
        }

        // X-Achse Beschriftungen
        xLabels.forEachIndexed { index, label ->
            val x = startX + (xStep * index) + (barWidth.toPx() / 2)
            val y = startY + 16.dp.toPx()

            drawContext.canvas.nativeCanvas.drawText(
                label,
                x,
                y,
                Paint().apply {
                    textSize = labelSize.toPx()
                    color = labelColor.toArgb()
                    textAlign = Paint.Align.CENTER
                }
            )
        }

        // Y-Achse Beschriftungen
        yLabels.forEachIndexed { index, label ->
            val x = startX - 40.dp.toPx()
            val y = startY - (yStep * index) + 6.dp.toPx()

            drawContext.canvas.nativeCanvas.drawText(
                label.toString(),
                x,
                y,
                Paint().apply {
                    textSize = labelSize.toPx()
                    color = labelColor.toArgb()
                    textAlign = Paint.Align.RIGHT
                }
            )
        }
    }
}

fun getChartData(chart: String): List<Float> {
    if(chart == "offer"){
        return listOf(80f, 65f, 90f, 75f, 30f)
    }else if(chart == "customer"){
        return listOf(180f, 165f, 190f, 175f, 130f)
    }else if(chart == "score"){
        return listOf(8000f, 6500f, 9000f, 7500f, 3000f)
    }else
        return listOf(0f,0f,0f,0f,0f)
}
fun getChartMax(chart: String): Float {
    if(chart == "offer"){
        return 100f
    }else if(chart == "customer"){
        return 200f
    }else if(chart == "score"){
        return 10000f
    }else
        return 0f
}

fun getyLabels(chart: String): List<Float> {
    if(chart == "offer"){
        return listOf(0f, 25f, 50f, 75f, 100f)
    }else if(chart == "customer"){
        return listOf(0f, 50f, 100f, 150f, 200f)
    }else if(chart == "score"){
        return listOf(0f, 2500f, 5000f, 7500f, 10000f)
    }else
        return listOf(0f,0f,0f,0f,0f)
}



@Composable
fun ScoreCircle(score: Int) {
    androidx.compose.material.Surface(
        modifier = Modifier
            .size(200.dp)
            .clip(CircleShape)
            .background(Color.Blue)
            .border(width = 2.dp, color = Color.Black, shape = CircleShape),
        color = Color.White,
        elevation = 4.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            androidx.compose.material.Text(
                text = score.toString(),
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ScoreLogo() {
    Box(contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(R.mipmap.img_1),
            contentDescription = "Logo",
            modifier = Modifier
        )
        Text(
            text = "999",
            fontSize = 70.sp,
            fontWeight = FontWeight.Bold,
            color = GreenDark
        )
    }
}

@Composable
fun CustomerScoreFields(selectedChart: MutableState<String>) {

    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        // Customer field
        Field(
            text = "Customer",
            selected = selectedChart.value == "customer",
            onClick = {
                selectedChart.value = "customer"
            }
        )
        Spacer(modifier = Modifier.weight(1F))
        // Score field
        Field(
            text = "Score",
            selected = selectedChart.value == "score",
            onClick = {
                selectedChart.value = "score"
            }
        )
        Spacer(modifier = Modifier.weight(1F))
        // Offer field
        Field(
            text = "Offer",
            selected = selectedChart.value == "offer",
            onClick = {
                selectedChart.value = "offer"
            }
        )
    }
    ChartSesion.chart = selectedChart.value
}

@Composable
fun Field(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (selected) GreenLight else Color.Transparent

    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 5.dp)
            .width(90.dp)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material.Text(
            text = text,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color.Black
        )
    }
}

object ChartSesion{
    var chart: String = "customer"
}