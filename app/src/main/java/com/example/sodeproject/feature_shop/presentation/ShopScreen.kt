package com.example.sodeproject.feature_shop.presentation

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sodeproject.feature_navigation.BottomNavigationBar
import com.example.sodeproject.util.standardQuadFromTo
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sodeproject.R
import com.example.sodeproject.feature_shop.data.Shop
import com.example.sodeproject.feature_shop.data.ShopSession
import com.example.sodeproject.ui.theme.GrayLight
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark

@Composable
fun ShopScreen(
    navController: NavController,
    viewModel: ShopViewModel = hiltViewModel()) {
    val shopState = viewModel.shopState.collectAsState(initial = null)
    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Column() {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                text = "Challanges",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                for (i in 1..3) {
                    ChallangeItem()
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 7.5.dp),
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
                text = "Shops"
            )
            Spacer(modifier = Modifier.height(16.dp))

            if(shopState.value?.isLoading == true){
                CircularProgressIndicator()
            } else {
                if(shopState.value?.isError == true){
                    Text(text = "Error downloading score: ${shopState.value?.isError}")
                }else{
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(start = 7.5.dp,end = 7.5.dp, bottom = 100.dp),
                        modifier = Modifier.fillMaxSize()
                    ){
                        items(ShopSession.shoplist.size){
                            ShopItem(navController, shop = ShopSession.shoplist[it])
                        }
                    }
                }
            }
        }

        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun ChallangeItem(){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.5.dp, horizontal = 7.5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(GreenMain)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(7.5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(color = Color.White, text = "lower asdad dasdasd werrfa")
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Check",
                        tint = GrayLight,
                    )
                }

            }
        }
    }
}

@Composable
fun ShopItem(
    navController: NavController,
    shop: Shop
){
    BoxWithConstraints(
        modifier = Modifier
            .padding(7.5.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(10.dp))
            .background(GreenSuperDark)
    ) {
        val width = constraints.maxWidth
        val height = constraints.maxHeight

        //Medium colored Path
        val mediumColoredPoint1 = Offset(0f, height * 0.3f)
        val mediumColoredPoint2 = Offset(width * 0.1f, height * 0.35f)
        val mediumColoredPoint3 = Offset(width * 0.4f, height * 0.05f)
        val mediumColoredPoint4 = Offset(width * 0.75f, height * 0.7f)
        val mediumColoredPoint5 = Offset(width * 1.4f, -height.toFloat())

        val mediumColoredPath = Path().apply {
            moveTo(mediumColoredPoint1.x,mediumColoredPoint1.y)
            standardQuadFromTo(mediumColoredPoint1, mediumColoredPoint2)
            standardQuadFromTo(mediumColoredPoint2, mediumColoredPoint3)
            standardQuadFromTo(mediumColoredPoint3, mediumColoredPoint4)
            standardQuadFromTo(mediumColoredPoint4, mediumColoredPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }

        // Light colored path
        val lightPoint1 = Offset(0f, height * 0.35f)
        val lightPoint2 = Offset(width * 0.1f, height * 0.4f)
        val lightPoint3 = Offset(width * 0.3f, height * 0.35f)
        val lightPoint4 = Offset(width * 0.65f, height.toFloat())
        val lightPoint5 = Offset(width * 1.4f, -height.toFloat() / 3f)

        val lightColoredPath = Path().apply {
            moveTo(lightPoint1.x, lightPoint1.y)
            standardQuadFromTo(lightPoint1, lightPoint2)
            standardQuadFromTo(lightPoint2, lightPoint3)
            standardQuadFromTo(lightPoint3, lightPoint4)
            standardQuadFromTo(lightPoint4, lightPoint5)
            lineTo(width.toFloat() + 100f, height.toFloat() + 100f)
            lineTo(-100f, height.toFloat() + 100f)
            close()
        }
        Canvas(
            modifier = Modifier
                .fillMaxSize()
        ) {
            drawPath(
                path = mediumColoredPath,
                color = GreenMain
            )
            drawPath(
                path = lightColoredPath,
                color = GreenLight
            )
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(15.dp)
        ) {
            Text(
                text = shop.name,
                fontSize =  if (shop.name.length > 14 || (!shop.name.contains(" ") && shop.name.length > 8)) {
                                32.sp
                            } else {
                                42.sp
                            },
                color = Color.White,
                style = MaterialTheme.typography.h2,
                //lineHeight = 7.sp,
                //modifier = Modifier.align(Alignment.TopStart)
            )
            Image(
                bitmap = base64ToImageBitmap(imageBase64 = aldiImage),
                contentDescription = "Bildbeschreibung",
                modifier = Modifier.align(Alignment.BottomStart).size(25.dp, 25.dp)
            )
            /*
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = shop.shopDescription,
                tint = Color.White,
                modifier = Modifier.align(Alignment.BottomStart)
            )

             */
            Text(
                text = "Info",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable {
                        ActiveInfoShop.shop = shop
                        navController.navigate("ShopInfo_Screen")
                    }
                    .align(Alignment.BottomEnd)
                    .clip(RoundedCornerShape(10.dp))
                    .background(GreenSuperDark)
                    .padding(vertical = 6.dp, horizontal = 15.dp)
            )
        }
    }
}
@Composable
fun base64ToImageBitmap(imageBase64: String): ImageBitmap {
    try {
        val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
    }catch (e: Exception){
        return ImageBitmap.imageResource(id = R.drawable.ic_broken_image)
    }
}

val aldiImage: String =  "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"