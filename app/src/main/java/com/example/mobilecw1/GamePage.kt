package com.example.mobilecw1

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun GamePage() {
    val viewModel = remember { GameViewModal() }

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
                    viewModel.CalculateScore()

                },
                modifier = Modifier.padding(8.dp).height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800))
            ) {
                Text(text = "Score", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
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



