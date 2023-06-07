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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.ShaderBrush
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sodeproject.R
import com.example.sodeproject.feature_login.data.UserSession
import com.example.sodeproject.feature_scanner.data.ShopArticle
import com.example.sodeproject.feature_shop.data.Challenges
import com.example.sodeproject.feature_shop.data.Shop
import com.example.sodeproject.feature_shop.data.ShopSession
import com.example.sodeproject.ui.theme.GrayLight
import com.example.sodeproject.ui.theme.GreenDark
import com.example.sodeproject.ui.theme.GreenLight
import com.example.sodeproject.ui.theme.GreenMain
import com.example.sodeproject.ui.theme.GreenSuperDark
import com.example.sodeproject.ui.theme.GreenSuperLight
import com.example.sodeproject.util.calculateHightFactor
import com.example.sodeproject.util.calculateSizeFactor

@Composable
fun ShopScreen(
    navController: NavController,
    viewModel: ShopViewModel = hiltViewModel()) {
    val shopState = viewModel.shopState.collectAsState(initial = null)
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ){
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val fac = calculateSizeFactor(screenWidth)
        val screenHeight = LocalConfiguration.current.screenHeightDp.dp
        val hfac = calculateHightFactor(screenHeight)
        Column() {
            if(UserSession.seller == false || UserSession.seller == null) {
                Column() {
                    if(shopState.value?.isLoading == true){
                        Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = GreenLight)
                        }
                    } else {
                        if(shopState.value?.isError == true){
                            Text(text = "Error downloading score: ${shopState.value?.isError}")
                        }else{
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                fontSize = 26.sp * fac,
                                fontWeight = FontWeight.Bold,
                                text = "Challenges",
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally),
                                color = GreenLight
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Column {
                                for (i in ShopSession.challangelist) {
                                    ChallangeItem(fac,i)
                                }
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                modifier = Modifier.padding(horizontal = 7.5.dp),
                                fontSize = 23.sp * fac,
                                fontWeight = FontWeight.Bold,
                                text = "Shops",
                                color = GreenLight
                            )
                            Spacer(modifier = Modifier.height(16.dp))

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
            }else{
                Column() {
                    if (shopState.value?.isLoading == true) {
                        CircularProgressIndicator()
                    } else {
                        if (shopState.value?.isError == true) {
                            Text(text = "Error downloading score: ${shopState.value?.isError}")
                        } else {
                            ShopShop(fac, hfac)
                        }
                    }
                }
            }
        }

        BottomNavigationBar(navController = navController)
    }
}

@Composable
fun ChallangeItem(fac: Float, i: Challenges){
    Box(modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 4.5.dp, horizontal = 7.5.dp)
        .clip(RoundedCornerShape(10.dp))
        .background(GreenLight)
    ){
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(7.5.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(color = Color.White, text = i.description, fontSize = 14.sp * fac, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(16.dp))
                Box(modifier = Modifier.padding(start = 8.dp)) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Check",
                        tint = if (!i.done) GrayLight else GreenSuperLight,
                        modifier = Modifier.size(24.dp * fac)
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
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .size(width.dp * 1 / 16 * fac, height.dp * 1 / 16 * fac)
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

@Composable
fun ShopShop(wfac: Float, hfac: Float){
    val fac =  1
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
                        moveTo(x - 350 * fac, y - 200 * fac)
                        lineTo(x - 350 * fac, y - 750 * fac)
                        //lineTo(x + 200, y - 600)
                        cubicTo(
                            x1 = x - 350 * fac,
                            y1 = y - 850 * fac,
                            x2 = x - 250 * fac,
                            y2 = y - 850 * fac,
                            x3 = x - 150 * fac,
                            y3 = y - 850 * fac
                        )
                        lineTo(x + 150 * fac, y - 850 * fac)
                        cubicTo(
                            x1 = x + 250 * fac,
                            y1 = y - 850 * fac,
                            x2 = x + 350 * fac,
                            y2 = y - 850 * fac,
                            x3 = x + 350 * fac,
                            y3 = y - 750 * fac
                        )
                        lineTo(x + 350 * fac, y - 200 * fac)
                        cubicTo(
                            x1 = x + 350 * fac,
                            y1 = y - 150 * fac,
                            x2 = x + 300 * fac,
                            y2 = y - 150 * fac,
                            x3 = x + 250 * fac,
                            y3 = y - 150 * fac
                        )
                        lineTo(x - 250 * fac, y - 150 * fac)
                        cubicTo(
                            x1 = x - 300 * fac,
                            y1 = y - 150 * fac,
                            x2 = x - 350 * fac,
                            y2 = y - 150 * fac,
                            x3 = x - 350 * fac,
                            y3 = y - 200 * fac
                        )
                    }
                    drawPath(path = path, color = androidx.compose.ui.graphics.Color.White)
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = 90.dp, bottom = 65.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    bitmap = base64ToImageBitmap(
                        imageBase64 = ActiveInfoShop.shop.logo
                    ),
                    contentDescription = "Bildbeschreibung",
                    modifier = Modifier.size(210.dp * wfac, 210.dp * wfac)
                )
                Spacer(modifier = Modifier.height(24.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                                Text(
                                    text = "Your Offer:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp * wfac,
                                    color = GreenSuperDark
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = ActiveInfoShop.shop.offer,
                                    color = GreenMain,
                                    fontSize = 17.sp * wfac
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Price: ${ActiveInfoShop.shop.offerCost} Points",
                                    color = GreenMain,
                                    fontSize = 17.sp * wfac
                                )
                            }
                        }
                        Box(
                            modifier = Modifier.fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color.White)
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                                Text(
                                    text = "Your Products:",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 25.sp * wfac,
                                    color = GreenSuperDark
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ActiveInfoShop.articleList.forEach { item ->
                                    ArticleItem1(item, wfac)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                    }
                }
            }
        }
    }
}

@Composable
fun ArticleItem1(
    article: ShopArticle,
    wfac: Float
){
    Row() {
        Text(text = "${article.scorePoints} Points for: ${article.description}", color = GreenMain, fontSize = 17.sp * wfac)
    }
}
