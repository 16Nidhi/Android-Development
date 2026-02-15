package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.delay

class ACTIVITY3 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                CampusFeedbackApp()
            }
        }
    }
}

enum class CampusScreen {
    Splash, Menu, Department, Subjects, Rating, Success
}

@Composable
fun CampusFeedbackApp() {
    var currentScreen by remember { mutableStateOf(CampusScreen.Splash) }

    when (currentScreen) {
        CampusScreen.Splash -> SplashScreen { currentScreen = CampusScreen.Menu }
        CampusScreen.Menu -> MenuScreen(
            onAboutClick = { },
            onSettingsClick = { },
            onExitClick = { },
            onNext = { currentScreen = CampusScreen.Department }
        )
        CampusScreen.Department -> DepartmentScreen(onGiveFeedback = { currentScreen = CampusScreen.Subjects })
        CampusScreen.Subjects -> SubjectsGridScreen(onNext = { currentScreen = CampusScreen.Rating })
        CampusScreen.Rating -> RatingScreen(onSubmit = { currentScreen = CampusScreen.Success })
        CampusScreen.Success -> SuccessScreen(onReset = { currentScreen = CampusScreen.Splash })
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(3000)
        onTimeout()
    }
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = Icons.Default.Face, 
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                tint = Color(0xFF6200EE)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Campus Feedback App",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "loading...", fontSize = 16.sp, color = Color.Gray)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CampusFeedbackPreview() {
    CSE225AndroidTheme {
        CampusFeedbackApp()
    }
}
