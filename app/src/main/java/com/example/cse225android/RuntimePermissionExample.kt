package com.example.cse225android

/*import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import java.util.jar.Manifest*/

import android.Manifest
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
import com.google.accompanist.permissions.*



class RuntimePermissionExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                CameraPermissionScreen()
            }
            }
        }
    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CameraPermissionScreen() {

    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when {
            cameraPermissionState.status.isGranted -> {
                Text("Camera permission granted ✅")
            }

            cameraPermissionState.status.shouldShowRationale -> {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Text("Camera access is needed to take photos")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = {
                        cameraPermissionState.launchPermissionRequest()
                    }) {
                        Text("Grant Permission")
                    }
                }
            }

            else -> {
                Button(onClick = {
                    cameraPermissionState.launchPermissionRequest()
                }) {
                    Text("Request Camera Permission")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCameraPermissionScreen() {
    CameraPermissionScreen()
}