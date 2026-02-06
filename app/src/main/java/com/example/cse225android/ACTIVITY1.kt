package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class ACTIVITY1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CSE225AndroidTheme {
                CricketScreen()
            }
        }
    }
}

@Composable
fun CricketScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFF1B5E20)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1B5E20))
        ) {
            Text(
                text = "Top 10 ICC Cricket Teams:",
                textAlign = TextAlign.Center,
                fontSize = 24.sp,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )
        }

        Image(
            painter = painterResource(id = R.drawable.cricket),
            contentDescription = "Cricket",
            modifier = Modifier.fillMaxWidth().height(200.dp)
                       .background(Color(0xFF1B5E20))
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RectangleShape,
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Text(
                text = "Top 10 ICC Cricket Teams:",
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
            )
        }

        val teams = listOf("India", "New Zealand", "Australia", "England", "South Africa",
            "Pakistan", "Bangladesh", "Sri Lanka", "West Indies", "Afghanistan")

        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f)
                .background(Color.White).padding(horizontal = 16.dp)
        ) {
            items(teams) { teamName ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = teamName,
                            fontSize = 20.sp,
                            color = Color.Blue,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CricketPreview() {
    CSE225AndroidTheme {
        CricketScreen()
    }
}