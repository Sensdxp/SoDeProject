package com.example.sodeproject.feature_shop.presentation

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.User
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_scanner.data.ShopArticleSession
import com.example.sodeproject.feature_scanner.presentation.SelectableItem
import com.example.sodeproject.feature_shop.data.Shop
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.ui.theme.GreenSuperLight
import com.example.sodeproject.util.calculateHightFactor
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter


@Composable
fun ShopInfoScreen(
    navController: NavController,
    viewModel: ShopInfoViewModel = hiltViewModel()
) {
    val shopInfoState = viewModel.shopState.collectAsState(initial = null)

    Box(modifier = Modifier.fillMaxSize()) {
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val hfac = calculateHightFactor(screenHeight)
        val screenWidth = LocalConfiguration.current.screenHeightDp.dp
        val wfac = calculateHightFactor(screenWidth)

        if (shopInfoState.value?.isLoading == true) {
            CircularProgressIndicator()
        } else {
            if (shopInfoState.value?.isError == true) {
                Text(text = "Error downloading score: ${shopInfoState.value?.isError}")
            } else {
                ShopInfoItem(hfac, wfac)
            }
        }
        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun ShopInfoItem(hfac: Float, wfac: Float){
    val fac =  1 / wfac
    val showQRCodeDialog = remember { mutableStateOf(false) }
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
                    val x = size.center.x
                    val y = size.center.y + 70
                    val center = size.center * 6F / 8F
                    path.apply {
                        moveTo(x - 350 * fac, y - 200* fac)
                        lineTo(x - 350 * fac , y - 750* fac)
                        //lineTo(x + 200, y - 600)
                        cubicTo(
                            x1 = x -350* fac,
                            y1 = y -850* fac,
                            x2 = x -250* fac ,
                            y2 = y -850* fac,
                            x3 = x -150* fac ,
                            y3 = y -850* fac
                        )
                        lineTo(x + 150* fac, y - 850* fac)
                        cubicTo(
                            x1 = x + 250* fac,
                            y1 = y - 850* fac,
                            x2 = x + 350* fac,
                            y2 = y - 850* fac,
                            x3 = x + 350* fac,
                            y3 = y - 750* fac
                        )
                        lineTo(x +350* fac, y - 200* fac)
                        cubicTo(
                            x1 = x + 350* fac,
                            y1 = y - 150* fac,
                            x2 = x + 300* fac,
                            y2 = y - 150* fac,
                            x3 = x + 250* fac,
                            y3 = y - 150* fac
                        )
                        lineTo(x - 250* fac, y - 150* fac)
                        cubicTo(
                            x1 = x - 300* fac,
                            y1 = y - 150* fac,
                            x2 = x - 350* fac,
                            y2 = y - 150* fac,
                            x3 = x - 350* fac,
                            y3 = y - 200* fac
                        )
                    }
                    drawPath(path = path, color = androidx.compose.ui.graphics.Color.White)
                },
            contentAlignment = Alignment.Center
        ){
            Column(
                modifier= Modifier
                    .fillMaxHeight()
                    .padding(top = 95.dp * hfac * hfac, bottom = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                    Image(
                        bitmap = base64ToImageBitmap(
                            imageBase64 = ActiveInfoShop.shop.logo
                        ),
                        contentDescription = "Bildbeschreibung",
                        modifier = Modifier.size(170.dp * wfac, 170.dp * wfac)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row() {
                        androidx.compose.material3.Button(
                            onClick = { showQRCodeDialog.value = true },
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = GreenSuperDark,
                                contentColor = androidx.compose.ui.graphics.Color.White
                            ),
                        ) {
                            Text(
                                text = "Open Offer",
                                Modifier.padding(horizontal = 20.dp * wfac),
                                fontSize = 12.sp * wfac
                            )
                        }
                    }
                    if (showQRCodeDialog.value) {
                        QRCodeDialog(onDismiss = { showQRCodeDialog.value = false })
                    }
                    Spacer(modifier = Modifier.height(16.dp * hfac))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.Start
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Who is ${ActiveInfoShop.shop.name}?",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp * wfac,
                                color = GreenSuperDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = ActiveInfoShop.shop.shopDescription,
                                color = GreenMain,
                                fontSize = 12.sp * wfac
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Offer:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp * wfac,
                                color = GreenSuperDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = ActiveInfoShop.shop.offer,
                                color = GreenMain,
                                fontSize = 12.sp * wfac
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Price: ${ActiveInfoShop.shop.offerCost} Points",
                                color = GreenMain,
                                fontSize = 12.sp * wfac
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Products:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp * wfac,
                                color = GreenSuperDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            ActiveInfoShop.articleList.forEach { item ->
                                ArticleItem(item,wfac)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
            }
        }
    }
}

@Composable
fun QRCodeDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Offer QR-Code") },
        text = { QRCode() }, // Hier wird der QR-Code-Composable eingefügt
        confirmButton = {
            Button(
                onClick = { onDismiss() },
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = GreenSuperDark,
                    contentColor = androidx.compose.ui.graphics.Color.White
                )
            ) {
                Text("OK")
            }
        }
    )
}
@Composable
fun QRCode(){
    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ){
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    val size = 512
                    val hints = hashMapOf<EncodeHintType,Int>().also {
                        it[EncodeHintType.MARGIN] = 1
                    }
                    val bits = QRCodeWriter().encode(
                        UserSession.uid + "*" + ActiveInfoShop.shop.offerId,
                        BarcodeFormat.QR_CODE,
                        size,
                        size,
                        hints,
                    )
                    val bitmap =
                        Bitmap.createBitmap(size,size, Bitmap.Config.RGB_565).also {
                            for(x in 0 until size){
                                for(y in 0 until size){
                                    it.setPixel(
                                        x,
                                        y,
                                        if (bits[x,y]) Color.BLACK else Color.WHITE
                                    )
                                }
                            }
                        }
                    setImageBitmap(bitmap)
                }
            }, modifier = Modifier
                .width(300.dp)
                .height(300.dp)

        )
    }
}
@Composable
fun ArticleItem(
    article: ShopArticle,
    wfac: Float
){
    Row() {
        Text(text = "${article.scorePoints} Points for: ${article.description}", color = GreenMain, fontSize = 12.sp * wfac)
    }
}