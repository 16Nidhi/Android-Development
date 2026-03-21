package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class MaterialFoundationEx1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CustomComponentScreen()
                }
            }
        }
    }
}

@Composable
fun CustomComponentScreen() {
    // State for TextField (Declarative approach)
    var username by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // 1. CUSTOM TEXT COMPONENT using parameters
        CustomHeader(text = "Unit 4: Custom UI", textColor = Color(0xFF6200EE))

        // 2. CUSTOM TEXTFIELD (Creating custom TextField using parameters/Shape)
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Enter Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp) // Custom Shape
        )

        // 3. CUSTOM BUTTON with parameters (Shape, Color, Modifier)
        CustomButton(
            label = "Submit Data",
            backgroundColor = Color(0xFF03DAC5),
            shape = CutCornerShape(8.dp),
            onClick = { /* Handle click */ }
        )

        // 4. Using Modifier, Shape, and Color on a Box
        Box(
            modifier = Modifier
                .size(200.dp, 80.dp)
                .background(Color.Yellow, shape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp))
                .border(2.dp, Color.Black, shape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("Custom Shape & Modifier", fontWeight = FontWeight.Bold, color = Color.Black)
        }
    }
}

// Reusable Custom Text Component using parameters
@Composable
fun CustomHeader(text: String, textColor: Color) {
    Text(
        text = text,
        color = textColor,
        fontSize = 28.sp,
        fontWeight = FontWeight.ExtraBold,
        modifier = Modifier.padding(bottom = 10.dp)
    )
}

// Reusable Custom Button Component using parameters
@Composable
fun CustomButton(
    label: String,
    backgroundColor: Color,
    shape: Shape,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = backgroundColor),
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        Text(text = label, color = Color.Black, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}
