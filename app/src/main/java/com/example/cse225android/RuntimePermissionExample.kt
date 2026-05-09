package com.example.cse225android

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
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
    val context = LocalContext.current
    val cameraPermissionState = rememberPermissionState(
        permission = Manifest.permission.CAMERA
    )

    var hasRequestedPermission by remember { mutableStateOf(false) }

    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        capturedImage = bitmap
        if (bitmap != null) {
            Toast.makeText(context, "Photo Captured!",
                Toast.LENGTH_SHORT).show()
        }
    }

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {

    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (capturedImage != null) {
            Text(
                "Captured Preview",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Image(
                bitmap = capturedImage!!.asImageBitmap(),
                contentDescription = "Captured Image",
                modifier = Modifier.size(200.dp).clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(24.dp))
        }

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
                            description = "Everything is ready! Tap below to open the camera.",
                            buttonText = "Open Camera",
                            onButtonClick = { cameraLauncher.launch(null) },
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    cameraPermissionState.status.shouldShowRationale -> {
                        PermissionUIContent(
                            icon = Icons.Default.Info,
                            title = "Camera Required",
                            description = "The app needs camera access to capture moments. Please allow it.",
                            buttonText = "Allow Access",
                            onButtonClick = { 
                                hasRequestedPermission = true
                                cameraPermissionState.launchPermissionRequest() 
                            },
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }

                    else -> {
                        val isPermanentlyDenied = hasRequestedPermission && !cameraPermissionState.status.shouldShowRationale
                        
                        PermissionUIContent(
                            icon = if (isPermanentlyDenied) Icons.Default.Settings else Icons.Default.CameraAlt,
                            title = if (isPermanentlyDenied) "Permission Denied" else "Enable Camera",
                            description = if (isPermanentlyDenied) 
                                "You've denied permission permanently. Please enable it in Settings to continue." 
                            else "To start using camera features, we need your permission.",
                            buttonText = if (isPermanentlyDenied) "Open Settings" else "Request Permission",
                            onButtonClick = {
                                if (isPermanentlyDenied) {
                                    openAppSettings(context)
                                } else {
                                    hasRequestedPermission = true
                                    cameraPermissionState.launchPermissionRequest()
                                }
                            },
                            tint = if (isPermanentlyDenied) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

private fun openAppSettings(context: android.content.Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(intent)
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
