package com.example.mobilecw1

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun LandingPage(navController: NavController) {
    var showAboutPopup by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.drawable.landingpagebg),
                contentScale = ContentScale.FillBounds
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    navController.navigate(Routes.NewGame)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Blue),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(
                    text = "NEW GAME",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { showAboutPopup = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(
                    text = "ABOUT",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
    if (showAboutPopup){
        AboutPopup(onDismiss = { showAboutPopup = false })
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
            Column(modifier = Modifier.padding(8.dp)) {
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
