package com.example.mobilecw1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobilecw1.ui.theme.MobileCW1Theme

class MainActivity : ComponentActivity() {
    val viewModel by lazy {
        ViewModelProvider(this).get(GameViewModal::class.java)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            MobileCW1Theme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        GameApp(viewModel)
                    }
                }
            }
        }
    }
}

@Composable
fun GameApp(viewModal: GameViewModal){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination  = Routes.LandingPage , builder = {
        composable(Routes.LandingPage){
            LandingPage(navController)
        }
        composable(Routes.NewGame){
            GamePage(navController, viewModal)
        }

    })
}



