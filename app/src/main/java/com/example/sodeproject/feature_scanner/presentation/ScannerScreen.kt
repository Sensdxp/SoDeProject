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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ScannerScreen(
    navController: NavController,
    scannerViewModel: ScannerViewModel = hiltViewModel()
) {
    val scannerState = scannerViewModel.scannerState.collectAsState(initial = null)
    // QR Code Generator
    if(UserSession.seller == false || UserSession.seller == null) {
        Row (
            modifier = Modifier.fillMaxSize(),
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
                            UserSession.uid,
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
    // QR Code Scanner
    }else{
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if(scannerState.value?.isLoading == true){
                CircularProgressIndicator()
            } else {
                if(scannerState.value?.isError == true){
                    Text(text = "Error downloading articles: ${scannerState.value?.isError}")
                }else{
                    Column {
                        ShopArticleSession.articleList.forEach { item ->
                            SelectableItem(item)
                        }
                        Button(
                            onClick = {
                                navController.navigate("QRScanner_Screen")
                            },
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        ) {
                            Text(text = "Bestätigen")
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


    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = shopArticle.description, modifier = Modifier.weight(1f))
        IconButton(
            onClick = {
                itemCount.value--
                // Subtract score points from the total volume
                ShopArticleSession.addPoints = ShopArticleSession.addPoints - shopArticle.scorePoints
                      },
            enabled = itemCount.value > 0
        ) {
            Icon(Icons.Filled.Email, contentDescription = "Minus")
        }
        Text(text = itemCount.value.toString())
        IconButton(
            onClick = {
                itemCount.value++
                // Add score points from the total volume
                ShopArticleSession.addPoints = ShopArticleSession.addPoints + shopArticle.scorePoints
            }
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Plus")
        }
    }
}

