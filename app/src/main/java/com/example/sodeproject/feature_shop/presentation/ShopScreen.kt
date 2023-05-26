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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sodeproject.R
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_shop.data.Shop
import com.example.sodeproject.feature_shop.data.ShopSession
import com.example.sodeproject.ui.theme.GrayLight
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.util.calculateSizeFactor

@Composable
fun ShopScreen(
    navController: NavController,
    viewModel: ShopViewModel = hiltViewModel()) {
    val shopState = viewModel.shopState.collectAsState(initial = null)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ){
        val screenWidth = maxWidth
        val fac = calculateSizeFactor(maxWidth)
        Column() {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                fontSize = 26.sp * fac,
                fontWeight = FontWeight.Bold,
                text = "Challanges",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column {
                for (i in 1..3) {
                    ChallangeItem(fac)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier.padding(horizontal = 7.5.dp),
                fontSize = 23.sp * fac,
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
                            ShopItem(navController, shop = ShopSession.shoplist[it], fac)
                        }
                    }
                }
            }
        }

        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun ChallangeItem(fac: Float){
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
                Text(color = Color.White, text = "lower asdad dasdasd werrfa", fontSize = 14.sp * fac)
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
    shop: Shop,
    fac: Float
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
                                32.sp * fac
                            } else {
                                42.sp * fac
                            },
                color = Color.White,
                style = MaterialTheme.typography.h2,
            )
            Image(
                bitmap = base64ToImageBitmap(imageBase64 = shop.logo),
                contentDescription = "Bildbeschreibung",
                modifier = Modifier.align(Alignment.BottomStart).size(width.dp * 1/16, height.dp * 1/16)
            )
            Text(
                text = "Info",
                color = Color.White,
                fontSize = 18.sp * fac,
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
    return try {
        val imageBytes = Base64.decode(imageBase64, Base64.DEFAULT)
        BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size).asImageBitmap()
    }catch (e: Exception){
        ImageBitmap.imageResource(id = R.drawable.ic_broken_image)
    }
}
