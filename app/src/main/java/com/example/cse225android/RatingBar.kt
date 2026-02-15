package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import java.util.concurrent.BlockingDeque

class RatingBar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                RatingApp()
                }
            }
        }
    }
@Composable
fun RatingApp() {
    var rating by remember { mutableStateOf(1.0) }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {
        Text(
            text = "Rate This App",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomRatingBar(
            rating = rating,
            onRatingChanged = { rating = it.toDouble() }
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Selected Rating: $rating Stars",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

    }
}
@Composable
fun CustomRatingBar(
    maxRating: Int = 5,
    rating: Double,
    onRatingChanged: (Int) -> Unit
) {
   Row{
       for (i in 1..maxRating) {
           Icon(
               imageVector = Icons.Default.Star,
               contentDescription = "Star $i",
               tint = if (i <= rating) Color(0xFFFFC107) else Color.Gray,
               modifier = Modifier.size(40.dp).clickable { onRatingChanged(i) }
           )
       }
   }


}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    CSE225AndroidTheme {
        RatingApp()
    }
}