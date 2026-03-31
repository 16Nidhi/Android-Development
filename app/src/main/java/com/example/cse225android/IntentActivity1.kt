package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class IntentActivity1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "screen1"
                    ) {
                        composable("screen1") { Screen1(navController) }
                        composable("screen2") { Screen2(navController) }
                        composable("screen3") { Screen3(navController) }
                        composable("screen4") { Screen4(navController) }
                        composable("screen5") { Screen5(navController) }
                    }
                }
            }
        }
    }
}

@Composable
fun Screen1(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = { navController.navigate("screen2") }) {
            Text("Next")
        }
    }
}

@Composable
fun Screen2(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { navController.navigate("screen3") }) {
            Text("Next")
        }
        
        Text(text = "1", fontSize = 60.sp)

        Button(onClick = { navController.navigate("screen1") }) {
            Text("Previous")
        }
    }
}

@Composable
fun Screen3(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { navController.navigate("screen4") }) {
            Text("Next")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "1", fontSize = 60.sp)
            Text(text = "2", fontSize = 60.sp)
        }

        Button(onClick = { navController.navigate("screen2") }) {
            Text("Previous")
        }
    }
}

@Composable
fun Screen4(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Button(onClick = { navController.navigate("screen5") }) {
            Text("Next")
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "1", fontSize = 60.sp)
            Text(text = "2", fontSize = 60.sp)
            Text(text = "3", fontSize = 60.sp)
        }

        Button(onClick = { navController.navigate("screen3") }) {
            Text("Previous")
        }
    }
}

@Composable
fun Screen5(navController: NavController) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = { navController.navigate("screen4") }) {
            Text("Previous")
        }
    }
}
