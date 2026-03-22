package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cse225android.ui.theme.CSE225AndroidTheme

/**
 * Activity demonstrating Custom UI Components in Jetpack Compose.
 * Covers Material & Foundation composables, declarative UI approach, 
 * and creating custom components using parameters, Modifiers, Shapes, and Colors.
 */
class MaterialFoundationEx1 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    CustomComponentScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomComponentScreen() {
    var username by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Compose Custom UI", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // 1. CUSTOM TEXT COMPONENT with refined styling
            CustomHeader(
                text = "Unit 4 Exploration",
                subtitle = "Mastering Custom Components",
                primaryColor = MaterialTheme.colorScheme.primary
            )

            // 2. STYLIZED BOX using Modifiers & Gradients (Custom Shape)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(Color(0xFF6200EE), Color(0xFF03DAC5))
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .border(1.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        "Custom Gradient Card",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            // 3. CUSTOM TEXTFIELD within a Card for better elevation
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("User Profile", fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Full Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            // 4. CUSTOM BUTTON with parameters & unique shape
            CustomButton(
                label = "GET STARTED",
                backgroundColor = MaterialTheme.colorScheme.primary,
                shape = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
                onClick = { /* Action */ }
            )
            
            Text(
                text = "Fully Declarative Approach",
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun CustomHeader(text: String, subtitle: String, primaryColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = text,
            color = primaryColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.sp
        )
        Text(
            text = subtitle,
            color = Color.Gray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider(
            color = primaryColor.copy(alpha = 0.3f),
            thickness = 2.dp,
            modifier = Modifier.width(60.dp)
        )
    }
}

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
            .padding(horizontal = 8.dp),
        elevation = ButtonDefaults.buttonElevation(8.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.2.sp
        )
    }
}
