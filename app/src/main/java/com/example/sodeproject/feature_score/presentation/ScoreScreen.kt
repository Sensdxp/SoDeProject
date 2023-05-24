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
import com.example.sodeproject.feature_score.data.ChartSession
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import java.text.DecimalFormat


@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel()
    ) {
    val scoreState = viewModel.scoreState.collectAsState(initial = null)
    val statsState = viewModel.statsState.collectAsState(initial = null)
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
            if (scoreState.value?.isLoading == true) {
                CircularProgressIndicator()
            } else {
                if (scoreState.value?.isError == true) {
                    Text(text = "Error downloading score: ${scoreState.value?.isError}")
                }else{
                    StoreScreen()
                }
            }
            }
        }
        BottomNavigationBar(navController = navController)
    }


@Composable
fun StoreScreen() {
    val selectedChart = remember { mutableStateOf("customer") }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White)
    ) {
        var test  = ChartSession.mCustomer.toList()
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
        return ChartSession.mOffer
    }else if(chart == "customer"){
        return ChartSession.mCustomer
    }else if(chart == "score"){
        return ChartSession.mScore
    }else
        return listOf(0f,0f,0f,0f,0f)
}
fun getChartMax(chart: String): Float {
    if(chart == "offer"){
        return convertChartMax(ChartSession.mOffer.max())
    }else if(chart == "customer"){
        return convertChartMax(ChartSession.mCustomer.max())
    }else if(chart == "score"){
        return convertChartMax(ChartSession.mScore.max())
    }else
        return 0f
}

fun convertChartMax(value: Float): Float {
    return when {
        value <= 20 -> 20f
        value <= 50 -> 50f
        value <= 100 -> 100f
        value <= 200 -> 200f
        value <= 500 -> 500f
        value <= 1000 -> 1000f
        value <= 2000 -> 2000f
        value <= 5000 -> 5000f
        value <= 10000 -> 10000f
        value <= 20000 -> 20000f
        value <= 50000 -> 50000f
        value <= 100000 -> 100000f
        else -> throw IllegalArgumentException("Value out of range") // Optional: Fehlerbehandlung für Werte außerhalb des definierten Bereichs
    }
}

fun getyLabels(chart: String): List<Float> {
    val number = getChartMax(chart)
    val resultList = mutableListOf<Float>()
    resultList.add(0f)
    resultList.add(number * 0.25f)
    resultList.add(number * 0.5f)
    resultList.add(number * 0.75f)
    resultList.add(number)
    return resultList
}

fun numberConverter(number: Int): String {
    val suffixes = listOf("", "K", "M", "B", "T") // Suffixe für 1000er-Schritte
    var convertedNumber = number.toDouble()
    var suffixIndex = 0

    while (convertedNumber >= 1000 && suffixIndex < suffixes.size - 1) {
        convertedNumber /= 1000
        suffixIndex++
    }

    val formattedNumber = when {
        convertedNumber >= 100 -> convertedNumber.toInt().toString() // Keine Dezimalstellen für Werte >= 100
        convertedNumber < 100 && convertedNumber >= 10 -> Math.floor(convertedNumber * 10)/10 // Eine Dezimalstelle für Werte >= 100
        else -> Math.floor(convertedNumber * 100)/100 // Zwei Dezimalstellen für Werte < 10
    }

    return "$formattedNumber${suffixes[suffixIndex]}"
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
            text = numberConverter(ChartSession.totalScore),
            fontSize = 69.sp,
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