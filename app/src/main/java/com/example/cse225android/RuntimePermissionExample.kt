package com.example.cse225android

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.cse225android.ui.theme.CSE225AndroidTheme
import com.google.accompanist.permissions.*

class RuntimePermissionExample : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CSE225AndroidTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CameraPermissionScreen()
                }
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
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.extraLarge,
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when {
                    cameraPermissionState.status.isGranted -> {
                        PermissionUIContent(
                            icon = Icons.Default.CheckCircle,
                            title = "Access Granted",
                            description = "You can now use the camera to take amazing photos!",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    cameraPermissionState.status.shouldShowRationale -> {
                        PermissionUIContent(
                            icon = Icons.Default.Info,
                            title = "Camera Required",
                            description = "The app needs camera access to function properly. Please grant permission.",
                            buttonText = "Allow Access",
                            onButtonClick = { cameraPermissionState.launchPermissionRequest() },
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    else -> {
                        PermissionUIContent(
                            icon = Icons.Default.CameraAlt,
                            title = "Enable Camera",
                            description = "To start using the camera features, we need your permission.",
                            buttonText = "Request Permission",
                            onButtonClick = { cameraPermissionState.launchPermissionRequest() },
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionUIContent(
    icon: ImageVector,
    title: String,
    description: String,
    buttonText: String? = null,
    onButtonClick: (() -> Unit)? = null,
    tint: androidx.compose.ui.graphics.Color
) {
    Icon(
        imageVector = icon,
        contentDescription = null,
        modifier = Modifier.size(80.dp),
        tint = tint
    )
    Spacer(modifier = Modifier.height(24.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.headlineSmall,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(12.dp))
    Text(
        text = description,
        style = MaterialTheme.typography.bodyLarge,
        textAlign = TextAlign.Center,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    if (buttonText != null && onButtonClick != null) {
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onButtonClick,
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = buttonText)
        }
    }
}

