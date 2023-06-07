package com.example.sodeproject.feature_scanner.presentation

import android.Manifest
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Size
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_scanner.data.QrCodeAnalyzer
import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_scanner.data.ShopArticleSession
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Button
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.util.calculateHightFactor
import androidx.compose.ui.graphics.Path
@Composable
fun ScannerScreen(
    navController: NavController,
    scannerViewModel: ScannerViewModel = hiltViewModel()
) {
    val scannerState = scannerViewModel.scannerState.collectAsState(initial = null)
    // QR Code Generator
    if(UserSession.seller == false || UserSession.seller == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(androidx.compose.ui.graphics.Color.White)
                .drawBehind {
                    val path = Path()
                    val x = size.center.x
                    val y = size.center.y
                    val center = size.center * 5F / 8F
                    path.apply {
                        moveTo(0f, 0f)
                        lineTo(x * 2, 0f)
                        lineTo(x * 2, 100f)
                        cubicTo(
                            x1 = x * 2 - 300,
                            y1 = 100f,
                            x2 = x,
                            y2 = y - 600,
                            x3 = 0f,
                            y3 = y - 600
                        )
                    }
                    drawPath(path = path, color = GreenLight)
                },
        ) {
            Column(
                Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp, start = 16.dp)) {
                Text(
                    text = "Your QR-Code!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = androidx.compose.ui.graphics.Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Buy smart",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = androidx.compose.ui.graphics.Color.White,
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "- SMARTbuy -",
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    color = GreenLight
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        factory = { ctx ->
                            ImageView(ctx).apply {
                                val size = 512
                                val hints = hashMapOf<EncodeHintType, Int>().also {
                                    it[EncodeHintType.MARGIN] = 1
                                }
                                val bits = QRCodeWriter().encode(
                                    UserSession.uid,
                                    BarcodeFormat.QR_CODE,
                                    size,
                                    size,
                                    hints,
                                )
                                val bitmap =
                                    Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565).also {
                                        for (x in 0 until size) {
                                            for (y in 0 until size) {
                                                it.setPixel(
                                                    x,
                                                    y,
                                                    if (bits[x, y]) Color.BLACK else Color.WHITE
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
                Text(
                    text = UserSession.userName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = GreenLight
                )
            }
        }
    // QR Code Scanner
    }else{
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            if(scannerState.value?.isLoading == true){
                CircularProgressIndicator()
            } else {
                if(scannerState.value?.isError == true){
                    Text(text = "Error downloading articles: ${scannerState.value?.isError}")
                }else{
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            modifier = Modifier.padding(top = 32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top,
                        ) {
                            Text(
                                text = "Select products",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                color = GreenLight
                            )
                            Text(
                                text = "Your products:",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 16.dp,bottom = 8.dp),
                                color = GreenLight
                            )

                            ShopArticleSession.articleList.forEach { item ->
                                SelectableItem(item)
                            }
                        }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(bottom = 80.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom,
                        ) {
                            Button(
                                onClick = {
                                    navController.navigate("QRScanner_Screen")
                                },
                                modifier = Modifier
                                    .padding(top = 20.dp, start = 15.dp, end = 15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    backgroundColor = GreenLight,
                                    contentColor = androidx.compose.ui.graphics.Color.White
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ) {
                                Text(
                                    text = "Confirm",
                                    fontSize = 20.sp,
                                    color = androidx.compose.ui.graphics.Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    BottomNavigationBar(navController = navController)
}

@Composable
fun SelectableItem(shopArticle: ShopArticle) {
    var itemCount =  remember { mutableStateOf(0) }

    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .background(color = GreenLight, shape = RoundedCornerShape(10.dp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 10.dp)
                .fillMaxWidth(),
        ) {
            Text(text = shopArticle.description, modifier = Modifier.weight(1f), fontSize = 18.sp, color = androidx.compose.ui.graphics.Color.White)
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White)
            ) {
                IconButton(
                    onClick = {
                        itemCount.value--
                        // Subtract score points from the total volume
                        ShopArticleSession.addPoints =
                            ShopArticleSession.addPoints - shopArticle.scorePoints
                    },
                    enabled = itemCount.value > 0
                ) {
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = "Minus",
                        tint = GreenDark
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = itemCount.value.toString(), fontSize = 18.sp, color = androidx.compose.ui.graphics.Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .size(25.dp)
                    .clip(CircleShape)
                    .background(androidx.compose.ui.graphics.Color.White)
            ) {
                IconButton(
                    onClick = {
                        itemCount.value++
                        // Add score points from the total volume
                        ShopArticleSession.addPoints =
                            ShopArticleSession.addPoints + shopArticle.scorePoints
                    }
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowUp,
                        contentDescription = "Plus",
                        tint = GreenDark
                    )
                }
            }
        }
    }
}

