package com.example.mobilecw1

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp

@Composable
fun GamePage(
    navController: NavController,
    viewModel: GameViewModal
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color.Black, Color(0xFF1C1C1C), Color(0xFF101820))
    )
    BoxWithConstraints {
        val isLandscape = maxWidth > maxHeight
        if (isLandscape) {
            Row(
                modifier = Modifier.fillMaxSize().background(gradientBrush).padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ScoreCard(viewModel, Modifier.weight(1f))
                Column (
                    modifier = Modifier.weight(1.5f),
                    verticalArrangement = Arrangement.SpaceBetween ,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    DiceSection(viewModel, Modifier.weight(1.5f))
                    ButtonSection(viewModel, Modifier.weight(1f), isLandscape)
                }

            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize().background(Color(0xFF101820)).padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScoreCard(viewModel, Modifier.fillMaxWidth())
                DiceSection(viewModel, Modifier.fillMaxWidth())
                ButtonSection(viewModel, Modifier.fillMaxWidth(), isLandscape)
            }
        }
        when {
        viewModel.showWonPopup -> {
            ResultPopup(
                title = "Congratulations!",
                message = "You won the game! 🎉",
                buttonColor = Color(0xff7cb342),
                secondaryButtonColor = Color(0xff7cb342),
                secondaryButtonVisible = true,
                lottieFile = R.raw.success_animation2,
                secondaryLottieFile = R.raw.fireworks,
                onDismiss = { viewModel.ShowPopupDismiss() },
                viewModel = viewModel,
                navController = navController,
                primaryButtonOnClick = { viewModel.ResetGame() },
                primaryButtonText = "PLAY AGAIN",
                isLandscape = isLandscape
            )
        }

        viewModel.showLosePopup -> {
            ResultPopup(
                title = "You Lost!",
                message = "Better luck next time! 😞",
                buttonColor = Color(0xFFD32F2F),
                secondaryButtonColor = Color(0xFFD32F2F),
                secondaryButtonVisible = true,
                lottieFile = R.raw.faill,
                secondaryLottieFile = null,
                onDismiss = { viewModel.ShowPopupDismiss() },
                viewModel = viewModel,
                navController = navController,
                primaryButtonOnClick = { viewModel.ResetGame() },
                primaryButtonText = "PLAY AGAIN",
                isLandscape = isLandscape,
                animationWidth = 300.dp

            )
        }

        viewModel.showTiePopup -> {
            Toast.makeText(LocalContext.current, "The game is a tie!", Toast.LENGTH_SHORT).show()
            ResultPopup(
                title = "Game Tie!",
                message = "It's a draw! You have another chance to win.",
                buttonColor = Color(0xff29b6f6),
                secondaryButtonColor = Color(0xff29b6f6),
                secondaryButtonVisible = false,
                lottieFile = R.raw.computer_and_human,
                secondaryLottieFile = null,
                onDismiss = { viewModel.ShowPopupDismiss() },
                viewModel = viewModel,
                navController = navController,
                primaryButtonOnClick = {},
                primaryButtonText = "CONTINUE",
                isLandscape = isLandscape,
                animationWidth = 300.dp
            )
        }
    }

        if (viewModel.showTargetPopup) {
            TargetScore(
                onDismiss = { viewModel.targetOnDismiss() },
                onConfirm = { targetScore ->
                    viewModel.updateTargetScore(targetScore)
                    viewModel.targetOnDismiss()
                },
                viewModel = viewModel,
                isLandscape = isLandscape
            )
        }
    }

}
@Composable
fun ScoreCard(viewModel: GameViewModal, modifier: Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .shadow(10.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scoreboard",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Human", viewModel.humanScore, Color(0xFF4CAF50))
                ScoreItem("Computer", viewModel.computerScore, Color(0xFFD32F2F))
            }
            Divider(color = Color.LightGray, thickness = 1.dp)
            Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ScoreItem("Wins 🏆", viewModel.humanWins, Color(0xFF2E7D32))
                ScoreItem("Losses 😢", viewModel.computerWins, Color(0xFFC62828))
            }
        }
    }
}

@Composable
fun ScoreItem(label: String, score: Int, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = score.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = color
        )
    }
}


@Composable
fun DiceSection(viewModel: GameViewModal, modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🎲 Human", fontSize = 18.sp, fontWeight = FontWeight.Bold,  color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            HumanList(viewModel.diceNoHuman, viewModel)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🤖 Computer", fontSize = 18.sp, fontWeight = FontWeight.Bold , color = Color.White)
            Spacer(modifier = Modifier.height(8.dp))
            ComputerList(viewModel.diceNoComputer)
        }
    }
}
@Composable
fun ButtonSection(viewModel: GameViewModal, modifier: Modifier, isLandscape: Boolean) {
    val turns by remember { derivedStateOf { viewModel.turns } }

    val enabledButtonColor = Color(0xFF4CAF50) // Green
    val scoreButtonColor = Color(0xFFFF9800) // Orange
    val disabledButtonColor = Color(0xFF424242) // Dark Gray (Same for all)
    val disabledTextColor = Color(0xFFBDBDBD) // Light Gray

    if (isLandscape) {
        Column(
            modifier = modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { viewModel.TrowOnclickaction() },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        enabled = turns == 0 || viewModel.isCalculationCompleted,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = enabledButtonColor,
                            disabledContainerColor = disabledButtonColor,
                            contentColor = Color.White,
                            disabledContentColor = disabledTextColor
                        )
                    ) {
                        Text(text = "🎲 Throw", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { viewModel.RerollOnclick() },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                            .weight(1f),
                        enabled = turns != 0,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = enabledButtonColor,
                            disabledContainerColor = disabledButtonColor,
                            contentColor = Color.White,
                            disabledContentColor = disabledTextColor
                        )
                    ) {
                        Text(text = "🔃 Re-roll", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = {
                            if (!viewModel.isCalculationCompleted) {
                                viewModel.CalculateScoreHumen()
                            }
                        },
                        modifier = Modifier
                            .padding(8.dp)
                            .height(50.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !viewModel.isCalculationCompleted,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = scoreButtonColor,
                            disabledContainerColor = disabledButtonColor,
                            contentColor = Color.White,
                            disabledContentColor = disabledTextColor
                        )
                    ) {
                        Text(text = "✅ Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = { viewModel.TrowOnclickaction() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = turns == 0 || viewModel.isCalculationCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = enabledButtonColor,
                    disabledContainerColor = disabledButtonColor,
                    contentColor = Color.White,
                    disabledContentColor = disabledTextColor
                )
            ) {
                Text(text = "🎲 Throw", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { viewModel.RerollOnclick() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = turns != 0,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = enabledButtonColor,
                    disabledContainerColor = disabledButtonColor,
                    contentColor = Color.White,
                    disabledContentColor = disabledTextColor
                )
            ) {
                Text(text = "🔃 Re-roll", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    if (!viewModel.isCalculationCompleted) {
                        viewModel.CalculateScoreHumen()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !viewModel.isCalculationCompleted,
                colors = ButtonDefaults.buttonColors(
                    containerColor = scoreButtonColor,
                    disabledContainerColor = disabledButtonColor,
                    contentColor = Color.White,
                    disabledContentColor = disabledTextColor
                )
            ) {
                Text(text = "✅ Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}


@Composable
fun HumanList(diceNo: List<Int>, viewModel: GameViewModal) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        diceNo.forEachIndexed { index, value ->
            HumanDiceGen(value, index, viewModel)
        }
    }
}
@Composable
fun HumanDiceGen(diceNo: Int, index: Int, viewModel: GameViewModal) {
    val isSelected = viewModel.isSelectCheck(diceNo,index)
    OutlinedButton(
        onClick = { viewModel.HumanDiceOnclick(diceNo,index)
            Log.d( "Human Selected Dice: ",viewModel.humanSelectedDice.toString())
        },
        border = if (isSelected) BorderStroke(2.dp, Color.Blue) else BorderStroke(0.dp, Color.Transparent),
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(0.dp),
        modifier = Modifier
            .background(if (isSelected) Color.LightGray else Color.Transparent , shape = RoundedCornerShape(20.dp))
            .padding(all = 0.dp),
    ) {
        Image(
            painter = painterResource(id = DiceGenerator(diceNo)),
            contentDescription = "Dice with number $diceNo",
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
        )
    }
}

@Composable
fun ComputerList(diceNo: List<Int>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        diceNo.forEach { ComputerDiceGen(it) }
    }
}

@Composable
fun ComputerDiceGen(diceNo: Int) {
    val diceImage = DiceGenerator(diceNo)
    Image(
        painter = painterResource(id = diceImage),
        contentDescription = "Dice with number $diceNo",
        modifier = Modifier
            .size(60.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(20.dp))
    )
}


fun DiceGenerator(diceNo: Int): Int {
    return when (diceNo) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}


@Composable
fun ResultPopup(
    title: String,
    message: String,
    buttonColor: Color,
    secondaryButtonColor: Color,
    secondaryButtonVisible: Boolean,
    lottieFile: Int,
    secondaryLottieFile: Int? = null,
    onDismiss: () -> Unit,
    viewModel: GameViewModal,
    navController: NavController,
    primaryButtonOnClick: () -> Unit,
    primaryButtonText: String,
    isLandscape: Boolean,
    animationWidth: Dp = 150.dp
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .border(1.dp, buttonColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                .widthIn(min = 1000.dp, max = 1500.dp)
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(2f)
                            .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = message,
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Button(
                                onClick = {
                                    primaryButtonOnClick()
                                    onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(buttonColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = primaryButtonText,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            if (secondaryButtonVisible) {
                                Button(
                                    onClick = {
                                        onDismiss()
                                        navController.navigate(Routes.LandingPage)
                                    },
                                    colors = ButtonDefaults.buttonColors(secondaryButtonColor),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(RoundedCornerShape(16.dp))
                                        .shadow(8.dp, RoundedCornerShape(16.dp)),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Text(
                                        text = "BACK TO HOME",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        textAlign = TextAlign.Center,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }
            }
            else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(24.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .height(200.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        secondaryLottieFile?.let {
                            val secondaryComposition by rememberLottieComposition(LottieCompositionSpec.RawRes(it))
                            val secondaryProgress by animateLottieCompositionAsState(
                                secondaryComposition,
                                iterations = LottieConstants.IterateForever
                            )

                            LottieAnimation(
                                composition = secondaryComposition,
                                progress = secondaryProgress,
                                modifier = Modifier
                                    .fillMaxWidth(1f)
                                    .height(200.dp)
                                    .align(Alignment.BottomCenter)
                            )
                        }


                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieFile))
                        val progress by animateLottieCompositionAsState(
                            composition,
                            iterations = LottieConstants.IterateForever
                        )

                        LottieAnimation(
                            composition = composition,
                            progress = progress,
                            modifier = Modifier.size(animationWidth)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = title,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                primaryButtonOnClick()
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(buttonColor),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = primaryButtonText,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (secondaryButtonVisible) {
                            Button(
                                onClick = {
                                    onDismiss()
                                    navController.navigate(Routes.LandingPage)
                                },
                                colors = ButtonDefaults.buttonColors(secondaryButtonColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .shadow(8.dp, RoundedCornerShape(16.dp)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text(
                                    text = "BACK TO HOME",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TargetScore(
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit,
    viewModel: GameViewModal,
    isLandscape: Boolean
) {
    var inputText by remember { mutableStateOf(viewModel.targetScore.toString()) } // Use String for input field

    Dialog(
        onDismissRequest = onDismiss,

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .background(Color(0xFF42A5F5).copy(alpha = 0.2f), shape = CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.target))
                            val progress by animateLottieCompositionAsState(
                                composition,
                                iterations = LottieConstants.IterateForever
                            )

                            LottieAnimation(
                                composition = composition,
                                progress = progress,
                                modifier = Modifier.size(100.dp)
                            )
                        }


                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {


                        OutlinedTextField(
                            value = inputText,
                            onValueChange = { newValue ->
                                if (newValue.all { char -> char.isDigit() }) {
                                    inputText = newValue
                                    viewModel.updateTargetScore(newValue.toIntOrNull() ?: 0)
                                }
                            },
                            label = { Text("Enter Target Score") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { onConfirm(viewModel.targetScore) },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = inputText.isNotEmpty()
                        ) {
                            Text("Set Target")
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xff8d6e63)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .shadow(8.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            enabled = inputText.isNotEmpty()
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            } else {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF42A5F5).copy(alpha = 0.2f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.target))
                        val progress by animateLottieCompositionAsState(
                            composition,
                            iterations = LottieConstants.IterateForever
                        )

                        LottieAnimation(
                            composition = composition,
                            progress = progress,
                            modifier = Modifier.size(100.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { newValue ->
                            if (newValue.all { char -> char.isDigit() }) {
                                inputText = newValue
                                viewModel.updateTargetScore(newValue.toIntOrNull() ?: 0)
                            }
                        },
                        label = { Text("Enter Target Score") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { onConfirm(viewModel.targetScore) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = inputText.isNotEmpty()
                    ) {
                        Text("Set Target")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff8d6e63)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .shadow(8.dp, RoundedCornerShape(16.dp)),
                        shape = RoundedCornerShape(16.dp),
                        enabled = inputText.isNotEmpty()
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}

