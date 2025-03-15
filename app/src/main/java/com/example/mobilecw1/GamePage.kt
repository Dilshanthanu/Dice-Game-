package com.example.mobilecw1

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController


@Composable
fun GamePage(
    navController: NavController) {
    val viewModel = remember { GameViewModal() }
    var showDialog by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Scores",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = "Human: ${viewModel.humanScore}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1565C0)
                    )
                    Text(
                        text = "Computer: ${viewModel.computerScore}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Human", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                HumanList(viewModel.diceNoHuman , viewModel)
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "Computer", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                ComputerList(viewModel.diceNoComputer)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    viewModel.TrowOnclickaction()
                          },
                modifier = Modifier.padding(8.dp).height(50.dp),
                enabled = viewModel.turns > 0,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text(text = "Throw", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = {
                    if (!viewModel.isCalculationCompleted) {
                        viewModel.CalculateScore()
                    }

                },
                modifier = Modifier.padding(8.dp).height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
                enabled = !viewModel.isCalculationCompleted
            ) {
                Text(text = "Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
    if (viewModel.showWonPopup){
        ResultPopup(
            title = "Congratulations!",
            message = "You won the game! 🎉",
            buttonColor = Color(0xFFFFA000),
            secondaryButtonColor = Color(0xFFFFA000),
            secondaryButtonVisible = true,
            imageRes = R.drawable.win_image,
            onDismiss = { viewModel.ShowPopupDismiss() },
            viewModel,
            navController,
            primaryButtonOnClick =  {viewModel.ResetGame()},
            primaryButtonText = "PLAY AGAIN"
        )
    }
    if (viewModel.showLosePopup){
        ResultPopup(
            title = "You Lost!",
            message = "Better luck next time! 😞",
            buttonColor = Color(0xFFD32F2F),
            secondaryButtonColor = Color(0xFFD32F2F),
            secondaryButtonVisible = true,
            imageRes = R.drawable.lose_image,
            onDismiss = {viewModel.ShowPopupDismiss() },
            viewModel,
            navController,
            primaryButtonOnClick =  {viewModel.ResetGame()},
            primaryButtonText = "PLAY AGAIN"

        )
    }
    if (viewModel.showTiePopup){
        Toast.makeText(LocalContext.current, "The game is a tie!", Toast.LENGTH_SHORT).show()
        ResultPopup(
            title = "Game Tie!",
            message = "It's a draw! You have another chance to win.",
            buttonColor = Color(0xff29b6f6),
            secondaryButtonColor = Color(0xff29b6f6),
            secondaryButtonVisible = false,
            imageRes = R.drawable.equal_image,
            onDismiss = {viewModel.ShowPopupDismiss() },
            viewModel,
            navController,
            primaryButtonOnClick = {},
            primaryButtonText = "CONTINUE"

        )
    }
    if (showDialog) {
        TargetScore(
            onDismiss = { showDialog = false },
            onConfirm = { targetScore ->
                viewModel.updateTargetScore(targetScore)
                showDialog = false
            },
            viewModel = viewModel
        )
    }


}


@Composable
fun HumanList(diceNo: List<Int>, viewModel: GameViewModal) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            .background(if (isSelected) Color.LightGray else Color.Transparent)
            .padding(all = 0.dp),
    ) {
        Image(
            painter = painterResource(id = DiceGenerator(diceNo)),
            contentDescription = "Dice with number $diceNo",
            modifier = Modifier
                .size(60.dp)
                .shadow(if (isSelected) 8.dp else 4.dp, shape = RoundedCornerShape(12.dp))
        )
    }

}

@Composable
fun ComputerList(diceNo: List<Int>) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
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
            .shadow(4.dp)
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
    imageRes: Int,
    onDismiss: () -> Unit,
    viewModel: GameViewModal,
    navController: NavController,
    primaryButtonOnClick: () -> Unit,
    primaryButtonText: String
) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.White, RoundedCornerShape(16.dp))
                .shadow(10.dp, RoundedCornerShape(16.dp))
                .border(1.dp, buttonColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(buttonColor.copy(alpha = 0.2f), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
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
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(Color(0xFF42A5F5), Color(0xFF1976D2))
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            primaryButtonText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
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
                                .shadow(8.dp, RoundedCornerShape(16.dp))
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(Color(0xFFFFA726), Color(0xFFF57C00))
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                "BACK TO HOME",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
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
    viewModel: GameViewModal // Fixed typo in ViewModel name
) {
    var inputText by remember { mutableStateOf(viewModel.targetScore.toString()) } // Use String for input field

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(Color.White, shape = RoundedCornerShape(12.dp))
        ) {
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
                    Image(
                        painter = painterResource(id = R.drawable.target_image),
                        contentDescription = "Target Icon",
                        modifier = Modifier.size(80.dp)
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
                        .shadow(8.dp, RoundedCornerShape(16.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFFFFA726), Color(0xFFF57C00))
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = inputText.isNotEmpty()
                ) {
                    Text("Set Target")
                }
            }
        }
    }
}
