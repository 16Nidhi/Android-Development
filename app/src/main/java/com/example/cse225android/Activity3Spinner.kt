package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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

class Activity3Spinner : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                DepartmentScreen(onGiveFeedback = {})
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DepartmentScreen(onGiveFeedback: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDept by remember { mutableStateOf("Select Department") }
    val departments = listOf("Android", "Data Sctructures", "AI", "DBMS","OS","Networks")
    val staffList = listOf(
        Pair("Dr. John Doe", "R001"),
        Pair("Prof. Jane Smith", "R002"),
        Pair("Dr. Alan Turing", "R003")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campus Feedback Bar", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2)
                )
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).fillMaxSize().padding(16.dp)) {
            Box(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = selectedDept,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Department") },
                    trailingIcon = {
                        IconButton(onClick = { expanded = true }) {
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }
                    },
                    modifier = Modifier.fillMaxWidth().clickable { expanded = true }
                )
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    departments.forEach { dept ->
                        DropdownMenuItem(text = { Text(dept) }, onClick = {
                            selectedDept = dept
                            expanded = false
                        })
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn {
                items(staffList) { staff ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(text = staff.first, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(text = "Roll: ${staff.second}", color = Color.Gray)
                            }
                            Button(
                                onClick = onGiveFeedback,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                            ) {
                                Text("Give Feedback")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DepartmentScreenPreview() {
    CSE225AndroidTheme {
        DepartmentScreen(onGiveFeedback = {})
    }
}
