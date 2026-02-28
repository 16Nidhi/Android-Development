package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import java.text.SimpleDateFormat
import java.util.*

class ProjectDatePicker : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                // Surface added for better appearance and theme support
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DatePickerApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerApp() {
    // State to control visibility of the DatePickerDialog
    var showDatePicker by remember { mutableStateOf(false) }
    // State to hold the formatted date string
    var selectedDate by remember { mutableStateOf("No date selected") }
    // State for the DatePicker itself
    val datePickerState = rememberDatePickerState()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Date: $selectedDate",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { showDatePicker = true }) {
            Text("Select Date")
        }
    }

    // Show the DatePickerDialog when showDatePicker is true
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    // Update the selectedDate state with the formatted date from the picker
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        // DatePicker works in UTC, so we set the formatter to UTC to avoid "off by one day" errors
                        formatter.timeZone = TimeZone.getTimeZone("UTC")
                        selectedDate = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            // The actual DatePicker UI inside the dialog
            DatePicker(state = datePickerState)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    CSE225AndroidTheme {
        DatePickerApp()
    }
}
