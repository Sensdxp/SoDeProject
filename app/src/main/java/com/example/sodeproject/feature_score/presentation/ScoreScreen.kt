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
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import com.example.sodeproject.R
import com.example.sodeproject.feature_scanner.data.Trans
import com.example.sodeproject.feature_score.data.ChartSession
import com.example.sodeproject.feature_shop.presentation.QRCode
import com.example.sodeproject.feature_shop.presentation.QRCodeDialog
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.ui.theme.GreenSuperLight
import com.example.sodeproject.util.calculateSizeFactor
import java.text.DecimalFormat


@Composable
fun ScoreScreen(
    navController: NavController,
    viewModel: ScoreViewModel = hiltViewModel()
    ) {
    val scoreState = viewModel.scoreState.collectAsState(initial = null)
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val fac = calculateSizeFactor(screenWidth)
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        if(UserSession.seller == false || UserSession.seller == null) {
            Column(Modifier.fillMaxSize()) {
                if (scoreState.value?.isLoading == true) {
                    CircularProgressIndicator()
                } else {
                    if (scoreState.value?.isError == true) {
                        Text(text = "Error downloading score: ${scoreState.value?.isError}")
                    }else{
                        UserScreen(fac)
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
                    StoreScreen(fac)
                }
            }
            }
        }
        BottomNavigationBar(navController = navController)
    }


@Composable
fun StoreScreen(fac: Float) {
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
                            text = "Hallo ${UserSession.userName}",
                            fontSize = 40.sp * fac,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        // Total Score Text
                        androidx.compose.material.Text(
                            text = "Your total score:",
                            fontSize = 32.sp * fac,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Score value inside a circle
                ScoreLogo(fac,ChartSession.totalScore)
                Spacer(modifier = Modifier.height(16.dp))
                // Customer and Score fields
                CustomerScoreFields(selectedChart, fac)
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
                                .height(200.dp * fac),
                            selectedChart = selectedChart.value,
                            fac = fac
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
    barColor: Color = Color.White,
    backgroundColor: Color = GreenLight,
    selectedChart: String,
    fac: Float
) {
    val barWidth: Dp = 22.dp * fac
    val barSpacing: Dp = 10.dp * fac
    val labelSize: Dp = 16.dp * fac

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
            val y = startY + 16.dp.toPx() * fac

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
fun ScoreLogo(fac: Float,number: Int) {
    Box(contentAlignment = Alignment.Center){
        Image(
            painter = painterResource(R.mipmap.img_1),
            contentDescription = "Logo",
            modifier = Modifier
        )
        Text(
            text = numberConverter(number),
            fontSize = 69.sp * fac,
            fontWeight = FontWeight.Bold,
            color = GreenDark
        )
    }
}

@Composable
fun CustomerScoreFields(selectedChart: MutableState<String>, fac: Float) {

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
            },
            fac = fac
        )
        Spacer(modifier = Modifier.weight(1F))
        // Score field
        Field(
            text = "Score",
            selected = selectedChart.value == "score",
            onClick = {
                selectedChart.value = "score"
            },
            fac = fac
        )
        Spacer(modifier = Modifier.weight(1F))
        // Offer field
        Field(
            text = "Offer",
            selected = selectedChart.value == "offer",
            onClick = {
                selectedChart.value = "offer"
            },
            fac = fac
        )
    }
    ChartSesion.chart = selectedChart.value
}

@Composable
fun Field(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    fac: Float
) {
    val backgroundColor = if (selected) GreenLight else Color.Transparent

    Box(
        modifier = Modifier
            .background(color = backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 5.dp)
            .width(90.dp * fac)
            .height(40.dp * fac),
        contentAlignment = Alignment.Center
    ) {
        androidx.compose.material.Text(
            text = text,
            fontSize = 18.sp * fac,
            fontWeight = FontWeight.Bold,
            color = if (selected) Color.White else Color.Black
        )
    }
}

object ChartSesion{
    var chart: String = "customer"
}
@Composable
fun UserScreen(fac: Float){
    val showDialog = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenLight)
            .drawBehind {
                val path = Path()
                val x = size.center.x
                val y = size.center.y + 200
                val center = size.center * 5F / 8F
                path.apply {
                    moveTo(0f, 0f)
                    lineTo(x * 2, 0f)
                    lineTo(x * 2, y + 100 * fac)
                    cubicTo(
                        x1 = x * 2,
                        y1 = y - 50 * fac,
                        x2 = x * 2 - 100 * fac,
                        y2 = y - 50 * fac,
                        x3 = x * 2 - 200 * fac,
                        y3 = y - 50 * fac
                    )
                    lineTo(200f * fac, y - 50 * fac)
                    cubicTo(
                        x1 = 100f * fac,
                        y1 = y - 50 * fac,
                        x2 = 0f * fac,
                        y2 = y - 50 * fac,
                        x3 = 0f,
                        y3 = y + 100 * fac
                    )
                }
                drawPath(path = path, color = Color.White)
            },
        contentAlignment = Alignment.Center
    ) {

        Column(
            Modifier
                .fillMaxHeight()
                .padding(top = 50.dp, bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserSession.score?.let { ScoreLogo(fac = fac, number = it) }
            Spacer(modifier = Modifier.height(46.dp*fac))
            Text(text = "Score from:",
                fontSize = 28.sp * fac,
                fontWeight = FontWeight.Bold,
                color = Color.White)
            Spacer(modifier = Modifier.height(8.dp*fac))
            Text(text = UserSession.userName,
                fontSize = 36.sp * fac,
                fontWeight = FontWeight.Bold,
                color = Color.White)
            Spacer(modifier = Modifier.height(16.dp*fac))
            Button(
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = GreenLight
                )
            ) {
                Text("Latest interaction",
                    fontSize = 25.sp * fac,
                    fontWeight = FontWeight.Bold,
                    color = GreenLight)
            }
            if (showDialog.value) {
                InteractionDialog(onDismiss = { showDialog.value = false },fac)
            }
        }
    }
}

@Composable
fun InteractionDialog(onDismiss: () -> Unit,fac: Float) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Latest interaction",color = GreenLight) },
        text = {
            Rechnung(fac)
        }, // Hier wird der QR-Code-Composable eingefügt
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = GreenSuperDark,
                    contentColor = Color.White
                )
            ) {
                Text("OK")
            }
        }
    )
}

@Composable
fun Rechnung(fac: Float){
    Box(modifier = Modifier.size(height = 250.dp*fac, width = 200.dp*fac)) {
        val list: List<Trans> = UserSession.trans
        LazyColumn() {
            items(list) { item ->
                Column(horizontalAlignment = Alignment.Start) {
                    Row() {
                        Text(text = item.datum, fontSize = 12.sp*fac, color = GreenLight)
                        Spacer(modifier = Modifier.width(8.dp*fac))
                        Text(text = item.punkte.toString(),modifier = Modifier.width(35.dp*fac), fontSize = 12.sp*fac, color = GreenLight)
                        Spacer(modifier = Modifier.width(8.dp*fac))
                        Text(text = item.shop, fontSize = 12.sp*fac, color = GreenLight)
                    }
                    Spacer(modifier = Modifier.height(8.dp*fac))
                }
            }
        }
    }
}