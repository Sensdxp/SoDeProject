package com.example.sodeproject.feature_shop.presentation

import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.feature_shop.data.Shop
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter


@Composable
fun ShopInfoScreen(
    navController: NavController
){

    Box(modifier = Modifier.fillMaxSize()){
        Column() {
            Text(text = ActiveInfoShop.shop.name)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = ActiveInfoShop.shop.shopDescription)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Offer:")
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = ActiveInfoShop.shop.offer)
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Price: ${ActiveInfoShop.shop.offerCost} Points")
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
                                UserSession.uid + ActiveInfoShop.shop.offerId,
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
        BottomNavigationBar(navController = navController)
    }
}