package com.example.sodeproject.feature_scanner.presentation

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Size
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.launch

@Composable
fun ScannerScreen(
    navController: NavController,
    scannerViewModel: ScannerViewModel = viewModel()

) {
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
                }, modifier = Modifier.width(300.dp).height(300.dp)

            )
        }
    }else{
        val context = LocalContext.current
        val lifecycleOwner = LocalLifecycleOwner.current
        val scope = rememberCoroutineScope()
        val cameraProviderFuture = remember {
            ProcessCameraProvider.getInstance(context)
        }

        val launcher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { granted ->
                scannerViewModel.hasCameraPermission.value = granted
            })
        LaunchedEffect(key1 = true) {
            if (!scannerViewModel.hasCameraPermission.value) {
                launcher.launch(Manifest.permission.CAMERA)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            AndroidView(
                factory = { context ->
                    val previewView = PreviewView(context)
                    val preview = Preview.Builder().build()
                    val selector = CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build()
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setTargetResolution(Size(previewView.width, previewView.height))
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                    imageAnalysis.setAnalyzer(
                        ContextCompat.getMainExecutor(context),
                        QrCodeAnalyzer { result ->
                            scannerViewModel.code.value = result
                        }
                    )
                    try {
                        cameraProviderFuture.get().bindToLifecycle(
                            lifecycleOwner,
                            selector,
                            preview,
                            imageAnalysis
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    previewView
                }, modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            LaunchedEffect(key1 = scannerViewModel.code.value != "") {
                scope.launch {
                    if (!scannerViewModel.code.value.equals("")) {
                        val success = scannerViewModel.code.value
                        Toast.makeText(context, "Code: ${success}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
    BottomNavigationBar(navController = navController)
}