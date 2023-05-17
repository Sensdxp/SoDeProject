package com.example.sodeproject.feature_scanner.presentation

import android.Manifest
import android.util.Size
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.sodeproject.feature_scanner.data.QrCodeAnalyzer
import kotlinx.coroutines.launch
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_scanner.data.ShopArticleSession

@Composable
fun QRScannerScreen(
    navController: NavController,
    viewModel: QRScannerViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val scope = rememberCoroutineScope()
    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            viewModel.hasCameraPermission.value = granted
        })
    LaunchedEffect(key1 = true) {
        if (!viewModel.hasCameraPermission.value) {
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
                        viewModel.code.value = result
                        val (userID, offerID) = splitString(result)
                        // Speichern der Ergebnisse in separaten Strings
                        val userIDString: String = userID
                        val offerIDString: String = offerID
                        if(offerIDString.isNotEmpty()){
                            ShopArticleSession.offerId = offerIDString
                            ShopArticleSession.shopId = UserSession.uid.toString()
                            ShopArticleSession.customerId = userIDString
                            navController.navigate("Offer_Screen")
                        }else{
                            viewModel.updateScore(ShopArticleSession.addPoints,userIDString)
                            navController.navigate("Scanner_Screen")
                        }
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

        LaunchedEffect(key1 = viewModel.code.value != "") {
            scope.launch {
                if (!viewModel.code.value.equals("")) {
                    val success = viewModel.code.value
                    Toast.makeText(context, "Code: ${success}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
    BottomNavigationBar(navController = navController)
}

fun splitString(inputString: String): Pair<String, String> {
    val parts = inputString.split("*")
    val userID = parts[0]
    val offerID = if (parts.size > 1) parts[1] else ""
    return Pair(userID, offerID)
}