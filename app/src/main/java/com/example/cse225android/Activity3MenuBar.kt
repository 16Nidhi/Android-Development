package com.example.cse225android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cse225android.ui.theme.CSE225AndroidTheme

class Activity3MenuBar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                MenuScreen(
                    onAboutClick = {},
                    onSettingsClick = {},
                    onExitClick = {},
                    onNext = {}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onAboutClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onExitClick: () -> Unit,
    onNext: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Campus Feedback Menu Bar") },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(text = { Text("About") }, onClick = { showMenu = false; onAboutClick() })
                        DropdownMenuItem(text = { Text("Settings") }, onClick = { showMenu = false; onSettingsClick() })
                        DropdownMenuItem(text = { Text("Exit") }, onClick = { showMenu = false; onExitClick() })
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)
            .fillMaxSize(),
            contentAlignment = Alignment.Center) {
            Button(onClick = onNext) {
                Text("Go to Next Screen")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MenuScreenPreview() {
    CSE225AndroidTheme {
        MenuScreen({}, {}, {}, {})
    }
}
