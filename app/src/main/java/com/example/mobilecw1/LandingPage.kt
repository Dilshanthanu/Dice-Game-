package com.example.mobilecw1

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.airbnb.lottie.compose.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit


@Composable
fun LandingPage(navController: NavController, viewModal: GameViewModal) {
    var showAboutPopup by remember { mutableStateOf(false) }

    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = LottieConstants.IterateForever
    )

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color.Black, Color(0xFF1C1C1C), Color(0xFF101820))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush)
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        if (isLandscape) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Game Logo",
                    modifier = Modifier
                        .size(250.dp)
                        .padding(end = 16.dp)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FancyButton(
                        text = "NEW GAME",
                        onClick = { navController.navigate(Routes.AnimationScreen) },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    )

                    if (!viewModal.isGameDataReset()) {
                        FancyButton(
                            text = "CONTINUE",
                            onClick = { navController.navigate(Routes.NewGame) },
                            modifier = Modifier.fillMaxWidth(0.5f)
                        )
                    }

                    FancyButton(
                        text = "ABOUT",
                        onClick = { showAboutPopup = true },
                        modifier = Modifier.fillMaxWidth(0.5f),
                        gradientColors = listOf(Color.Red, Color.Magenta)
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo2),
                    contentDescription = "Game Logo",
                    modifier = Modifier
                        .size(500.dp)
                        .padding(bottom = 16.dp)
                )

                if (!viewModal.isGameDataReset()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FancyButton(
                            text = "NEW GAME",
                            onClick = { navController.navigate(Routes.AnimationScreen) },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )

                        FancyButton(
                            text = "CONTINUE",
                            onClick = { navController.navigate(Routes.NewGame) },
                            modifier = Modifier.weight(1f).padding(start = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    FancyButton(
                        text = "ABOUT",
                        onClick = { showAboutPopup = true },
                        modifier = Modifier.fillMaxWidth(0.6f),
                        gradientColors = listOf(Color.Red, Color.Magenta)
                    )
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FancyButton(
                            text = "NEW GAME",
                            onClick = { navController.navigate(Routes.AnimationScreen) },
                            modifier = Modifier.weight(1f).padding(end = 8.dp)
                        )

                        FancyButton(
                            text = "ABOUT",
                            onClick = { showAboutPopup = true },
                            modifier = Modifier.weight(1f).padding(start = 8.dp),
                            gradientColors = listOf(Color.Red, Color.Magenta)
                        )
                    }
                }
            }
        }
    }

    if (showAboutPopup) {
        AboutPopup(onDismiss = { showAboutPopup = false })
    }
}

@Composable
fun FancyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    gradientColors: List<Color> = listOf(Color.Cyan, Color.Blue),
    backgroundColor: Color = Color(0x2FFFFFFF),
    textColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(20.dp),
    elevation: Dp = 6.dp,
    fontSize: TextUnit = 18.sp
) {
    Surface(
        shape = shape,
        color = backgroundColor.copy(alpha = 0.15f),
        shadowElevation = elevation,
        border = BorderStroke(2.dp, Brush.linearGradient(gradientColors)),
        modifier = modifier.clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .padding(vertical = 14.dp, horizontal = 24.dp)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = textColor,
                fontWeight = FontWeight.Bold,
                fontSize = fontSize
            )
        }
    }
}

@Composable
fun AboutPopup(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "About",
                    tint = Color(0xFF6200EE),
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp)
                )
                Text(
                    text = "About",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .heightIn(min = 100.dp, max = 300.dp)
            ) {
                Text(
                    text = "Name: T.G.Dilshan Thotapaldeniya",
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
                Text(
                    text = "Student ID: w1987529",
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "I confirm that I understand what plagiarism is and have read and " +
                            "understood the section on Assessment Offences in the Essential " +
                            "Information for Students. The work that I have submitted is " +
                            "entirely my own. Any work from other authors is duly referenced " +
                            "and acknowledged.",
                    fontSize = 16.sp,
                    color = Color(0xFF555555)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6200EE),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(48.dp)
            ) {
                Text(
                    text = "OK",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.1.sp
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(16.dp)
    )

}

