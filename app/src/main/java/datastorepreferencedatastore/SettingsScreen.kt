package datastorepreferencedatastore

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SecureSettingsViewModel) {
    val settings by viewModel.uiState.collectAsState()
    
    // Vibrant gradient background - definitely NOT white
    val mainGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFEEF2FF), // Very Light Indigo
            Color(0xFFE0E7FF), // Light Indigo
            Color(0xFFC7D2FE)  // Soft Indigo
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(mainGradient)
    ) {
        Scaffold(
            containerColor = Color.Transparent, // Critical: makes the Box background visible
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
                        scrolledContainerColor = Color.White.copy(alpha = 0.8f)
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
                PremiumProfileBanner(username = settings.username)

                // Personalization
                SettingsCategorySection(title = "Experience") {
                    SettingsToggleRow(
                        icon = Icons.Default.Palette,
                        iconColor = Color(0xFF6366F1),
                        title = "Dark Theme",
                        subtitle = "Switch to a darker interface",
                        checked = settings.isDarkMode,
                        onCheckedChange = { viewModel.updateDarkMode(it) }
                    )
                }

                // Account
                SettingsCategorySection(title = "Account Settings") {
                    var tempUsername by remember { mutableStateOf(settings.username) }
                    LaunchedEffect(settings.username) { tempUsername = settings.username }

                    SettingsInputRow(
                        icon = Icons.Default.PersonOutline,
                        iconColor = Color(0xFF10B981),
                        title = "Username",
                        value = tempUsername,
                        onValueChange = { tempUsername = it },
                        onSave = { viewModel.updateUsername(tempUsername) }
                    )
                }

                // Security
                SettingsCategorySection(title = "Privacy & Security") {
                    SettingsToggleRow(
                        icon = Icons.Default.Fingerprint,
                        iconColor = Color(0xFFEF4444),
                        title = "Biometric Lock",
                        subtitle = "Secure app with fingerprint",
                        checked = settings.fingerprintEnabled,
                        onCheckedChange = { viewModel.updateFingerprint(it) }
                    )
                    
                    var tempToken by remember { mutableStateOf(settings.privateToken) }
                    LaunchedEffect(settings.privateToken) { tempToken = settings.privateToken }

                    SettingsInputRow(
                        icon = Icons.Default.Https,
                        iconColor = Color(0xFF8B5CF6),
                        title = "Secret Token",
                        value = tempToken,
                        onValueChange = { tempToken = it },
                        onSave = { viewModel.updateToken(tempToken) },
                        isSensitive = true
                    )
                }

                // Notifications
                SettingsCategorySection(title = "System") {
                    SettingsToggleRow(
                        icon = Icons.Default.NotificationsNone,
                        iconColor = Color(0xFFF59E0B),
                        title = "Push Notifications",
                        subtitle = "Alerts and sound effects",
                        checked = settings.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
fun PremiumProfileBanner(username: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, RoundedCornerShape(32.dp)),
        shape = RoundedCornerShape(32.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .background(
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
fun SettingsCategorySection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF4F46E5),
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(start = 12.dp, bottom = 12.dp)
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = Color.White.copy(alpha = 0.9f),
            shadowElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(8.dp), content = content)
        }
    }
}

@Composable
fun SettingsToggleRow(
    icon: ImageVector,
    iconColor: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable { onCheckedChange(!checked) },
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle, color = Color.Gray, fontSize = 12.sp) },
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
    isSensitive: Boolean = false
) {
    var editing by remember { mutableStateOf(false) }

    Column {
        ListItem(
            modifier = Modifier.clip(RoundedCornerShape(16.dp)).clickable { editing = !editing },
            headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
            supportingContent = { 
                Text(
                    text = if (isSensitive && !editing) "••••••••" else if (value.isEmpty()) "Not set" else value,
                    color = Color(0xFF4F46E5)
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
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
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
