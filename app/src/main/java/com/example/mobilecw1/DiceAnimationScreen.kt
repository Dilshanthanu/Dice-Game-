package com.example.mobilecw1

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

@Composable
fun DiceAnimationScreen(navController: NavController, viewModel: GameViewModal) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.dice_roll))
    val progress by animateLottieCompositionAsState(
        composition,
        iterations = 1
    )
    Log.d("dwdwedw" , composition.toString())

    LaunchedEffect(progress) {
        if (progress == 1f) {
            navController.navigate(Routes.NewGame) {
                popUpTo(Routes.AnimationScreen) { inclusive = true }
            }
            viewModel.resetGameData()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(300.dp)
        )
    }
}
