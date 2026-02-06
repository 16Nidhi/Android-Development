package com.example.cse225android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import kotlinx.coroutines.delay

class ACTIVITY2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                Splash {
                    startActivity(Intent(this, Dashboard::class.java))
                    finish()
                }
            }
        }
    }
}

@Composable
fun Splash(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "My Student App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    CSE225AndroidTheme {
        Splash {}
    }
}