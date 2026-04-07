package datastorepreferencedatastore

import android.app.TimePickerDialog
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SecureSettingsViewModel) {
    val settings by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Resolve Dark Mode: Apply theme dynamically
    MaterialTheme(
        colorScheme = if (settings.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        val mainGradient = if (settings.isDarkMode) {
            Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF1E1E1E)))
        } else {
            Brush.verticalGradient(listOf(Color(0xFFEEF2FF), Color(0xFFE0E7FF), Color(0xFFC7D2FE)))
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(mainGradient)
        ) {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Text(
                                "Secure Settings",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-1).sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = { /* Handle back */ }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                            }
                        },
                        colors = TopAppBarDefaults.largeTopAppBarColors(
                            containerColor = Color.Transparent,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                            scrolledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                        )
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    // Profile Banner
                    PremiumProfileBanner(username = settings.username, isDarkMode = settings.isDarkMode)
                    
                    // General Section
                    SettingsCategorySection(title = "General", isDarkMode = settings.isDarkMode) {
                        val languages = listOf("English", "Hindi", "Spanish", "French", "German")
                        var selectedLanguage by remember { mutableStateOf(languages[0]) }
                        
                        SettingsSpinnerRow(
                            icon = Icons.Default.Language,
                            iconColor = Color(0xFF3B82F6),
                            title = "App Language",
                            options = languages,
                            selectedOption = selectedLanguage,
                            onOptionSelected = { selectedLanguage = it },
                            isDarkMode = settings.isDarkMode
                        )
                    }

                    // Sound & Notifications Section
                    SettingsCategorySection(title = "Sound & Notifications", isDarkMode = settings.isDarkMode) {
                        SettingsSliderRow(
                            icon = Icons.Default.VolumeUp,
                            iconColor = Color(0xFF8B5CF6),
                            title = "Notification Volume",
                            value = settings.notificationVolume,
                            onValueChange = { viewModel.updateNotificationVolume(it) },
                            isDarkMode = settings.isDarkMode
                        )
                        SettingsToggleRow(
                            icon = Icons.Default.NotificationsActive,
                            iconColor = Color(0xFFF43F5E),
                            title = "Push Notifications",
                            subtitle = "Receive alerts and updates",
                            checked = settings.notificationsEnabled,
                            onCheckedChange = { viewModel.updateNotifications(it) },
                            isDarkMode = settings.isDarkMode
                        )
                    }
                    
                    // Experience Section
                    SettingsCategorySection(title = "Experience", isDarkMode = settings.isDarkMode) {
                        SettingsToggleRow(
                            icon = Icons.Default.Palette,
                            iconColor = Color(0xFF6366F1),
                            title = "Dark Theme",
                            subtitle = "Switch to a darker interface",
                            checked = settings.isDarkMode,
                            onCheckedChange = { viewModel.updateDarkMode(it) },
                            isDarkMode = settings.isDarkMode
                        )
                    }
                    
                    // Account Section
                    SettingsCategorySection(title = "Account Settings", isDarkMode = settings.isDarkMode) {
                        var tempUsername by remember { mutableStateOf(settings.username) }
                        LaunchedEffect(settings.username) { tempUsername = settings.username }

                        SettingsInputRow(
                            icon = Icons.Default.PersonOutline,
                            iconColor = Color(0xFF10B981),
                            title = "Username",
                            value = tempUsername,
                            onValueChange = { tempUsername = it },
                            onSave = { viewModel.updateUsername(tempUsername) },
                            isDarkMode = settings.isDarkMode
                        )

                        val levels = listOf("Standard", "High", "Maximum")
                        SettingsSpinnerRow(
                            icon = Icons.Default.VerifiedUser,
                            iconColor = Color(0xFF10B981),
                            title = "Security Level",
                            options = levels,
                            selectedOption = settings.securityLevel,
                            onOptionSelected = { viewModel.updateSecurityLevel(it) },
                            isDarkMode = settings.isDarkMode
                        )
                    }
                    
                    // Privacy & Backup Section
                    SettingsCategorySection(title = "Privacy & Backup", isDarkMode = settings.isDarkMode) {
                        SettingsToggleRow(
                            icon = Icons.Default.Fingerprint,
                            iconColor = Color(0xFFEF4444),
                            title = "Biometric Lock",
                            subtitle = "Secure app with fingerprint",
                            checked = settings.fingerprintEnabled,
                            onCheckedChange = { viewModel.updateFingerprint(it) },
                            isDarkMode = settings.isDarkMode
                        )
                        
                        var backupTime by remember { mutableStateOf("02:00 AM") }
                        val calendar = Calendar.getInstance()
                        
                        val showTimePicker = {
                            TimePickerDialog(
                                context,
                                { _, hour, minute ->
                                    val amPm = if (hour < 12) "AM" else "PM"
                                    val formattedHour = if (hour % 12 == 0) 12 else hour % 12
                                    backupTime = String.format(Locale.getDefault(), "%02d:%02d %s", formattedHour, minute, amPm)
                                },
                                calendar.get(Calendar.HOUR_OF_DAY),
                                calendar.get(Calendar.MINUTE),
                                false
                            ).show()
                        }

                        SettingsItem(
                            icon = Icons.Default.Schedule,
                            iconBg = Color(0xFFF59E0B),
                            title = "Scheduled Backup",
                            description = "Current: $backupTime",
                            trailing = {
                                TextButton(onClick = showTimePicker) {
                                    Text("Change")
                                }
                            },
                            isDarkMode = settings.isDarkMode
                        )

                        SettingsItem(
                            icon = Icons.Default.CloudUpload,
                            iconBg = Color(0xFF0EA5E9),
                            title = "Manual Backup",
                            description = "Last backup: ${settings.lastBackupDate}",
                            trailing = {
                                Button(
                                    onClick = { viewModel.triggerManualBackup() },
                                    shape = RoundedCornerShape(12.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp),
                                    modifier = Modifier.height(36.dp)
                                ) {
                                    Text("Backup Now", fontSize = 12.sp)
                                }
                            },
                            isDarkMode = settings.isDarkMode
                        )

                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

                        SettingsItem(
                            icon = Icons.Default.DeleteForever,
                            iconBg = Color(0xFFEF4444),
                            title = "Reset All Settings",
                            description = "Clear all preferences and data",
                            trailing = {
                                IconButton(onClick = { viewModel.clearAllSettings() }) {
                                    Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color(0xFFEF4444))
                                }
                            },
                            isDarkMode = settings.isDarkMode
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
fun SettingsSliderRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    isDarkMode: Boolean
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(44.dp).background(iconColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                title, 
                fontWeight = FontWeight.SemiBold, 
                modifier = Modifier.weight(1f),
                color = if (isDarkMode) Color.White else Color.Unspecified
            )
            Text(
                "${(value * 100).toInt()}%", 
                style = MaterialTheme.typography.bodySmall, 
                color = if (isDarkMode) Color.LightGray else Color.Gray
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.padding(top = 8.dp),
            colors = SliderDefaults.colors(
                thumbColor = iconColor,
                activeTrackColor = iconColor,
                inactiveTrackColor = iconColor.copy(alpha = 0.24f)
            )
        )
    }
}

@Composable
fun SettingsSpinnerRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    isDarkMode: Boolean
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        ListItem(
            modifier = Modifier.clip(RoundedCornerShape(16.dp))
                .clickable { expanded = true },
            headlineContent = { 
                Text(title, fontWeight = FontWeight.SemiBold, color = if (isDarkMode) Color.White else Color.Unspecified) 
            },
            supportingContent = { Text(selectedOption, color = Color(0xFF4F46E5)) },
            leadingContent = {
                Box(
                    modifier = Modifier.size(44.dp)
                        .background(iconColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
                }
            },
            trailingContent = {
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.LightGray)
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(if (isDarkMode) Color(0xFF1E1E1E) else Color.White).width(200.dp)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, color = if (isDarkMode) Color.White else Color.Unspecified) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun PremiumProfileBanner(username: String, isDarkMode: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        color = if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    ) {
        Box(
            modifier = Modifier.background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFF4F46E5), Color(0xFF7C3AED))
                    )
                )
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(8.dp).fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Column {
                    Text("Settings for", style = MaterialTheme.typography.labelMedium, color = Color.White.copy(alpha = 0.8f))
                    Text(username, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun SettingsCategorySection(title: String, isDarkMode: Boolean, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = if (isDarkMode) Color(0xFF818CF8) else Color(0xFF4F46E5),
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = if (isDarkMode) Color(0xFF1E1E1E).copy(alpha = 0.9f) else Color.White.copy(alpha = 0.9f),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(8.dp), content = content)
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    iconBg: Color,
    title: String,
    description: String,
    trailing: @Composable () -> Unit,
    isDarkMode: Boolean
) {
    ListItem(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)),
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold, color = if (isDarkMode) Color.White else Color.Unspecified) },
        supportingContent = { Text(description, color = if (isDarkMode) Color.LightGray else Color.Gray, fontSize = 12.sp) },
        leadingContent = {
            Box(
                modifier = Modifier.size(44.dp).background(iconBg.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconBg, modifier = Modifier.size(22.dp))
            }
        },
        trailingContent = trailing,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    isDarkMode: Boolean
) {
    ListItem(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable { onCheckedChange(!checked) },
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold, color = if (isDarkMode) Color.White else Color.Unspecified) },
        supportingContent = { Text(subtitle, color = if (isDarkMode) Color.LightGray else Color.Gray, fontSize = 12.sp) },
        leadingContent = {
            Box(
                modifier = Modifier.size(44.dp).background(iconColor.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
            }
        },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
fun SettingsInputRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit,
    isSensitive: Boolean = false,
    isDarkMode: Boolean
) {
    var editing by remember { mutableStateOf(false) }

    Column {
        ListItem(
            modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable { editing = !editing },
            headlineContent = { Text(title, fontWeight = FontWeight.SemiBold, color = if (isDarkMode) Color.White else Color.Unspecified) },
            supportingContent = { 
                Text(
                    text = if (isSensitive && !editing) "••••••••" else value.ifEmpty { "Not set" },
                    color = if (isDarkMode) Color(0xFF818CF8) else Color(0xFF4F46E5)
                ) 
            },
            leadingContent = {
                Box(
                    modifier = Modifier.size(44.dp).background(iconColor.copy(alpha = 0.12f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(22.dp))
                }
            },
            trailingContent = {
                Icon(if (editing) Icons.Default.KeyboardArrowUp else Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
            },
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )

        AnimatedVisibility(visible = editing) {
            Row(
                modifier = Modifier.padding(16.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = if (isDarkMode) Color(0xFF2D2D2D) else Color.White,
                        unfocusedContainerColor = if (isDarkMode) Color(0xFF2D2D2D) else Color.White,
                        focusedTextColor = if (isDarkMode) Color.White else Color.Unspecified,
                        unfocusedTextColor = if (isDarkMode) Color.White else Color.Unspecified
                    )
                )
                Button(
                    onClick = { onSave(); editing = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                ) {
                    Text("Save")
                }
            }
        }
    }
}
