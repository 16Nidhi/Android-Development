package datastorepreferencedatastore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SecureSettingsViewModel) {
    val settings by viewModel.uiState.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        "Settings",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* Handle back */ }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Appearance Section
            SettingsCategory(title = "Appearance") {
                SettingsSwitchItem(
                    icon = Icons.Default.Palette,
                    title = "Dark Mode",
                    subtitle = "Adjust theme to your preference",
                    checked = settings.isDarkMode,
                    onCheckedChange = { viewModel.updateDarkMode(it) }
                )
            }

            // Profile Section
            SettingsCategory(title = "Profile") {
                var tempUsername by remember { mutableStateOf(settings.username) }
                LaunchedEffect(settings.username) { tempUsername = settings.username }

                SettingsInputItem(
                    icon = Icons.Default.Person,
                    title = "Username",
                    value = tempUsername,
                    onValueChange = { tempUsername = it },
                    onSave = { viewModel.updateUsername(tempUsername) }
                )
            }

            // Preferences Section
            SettingsCategory(title = "General Preferences") {
                SettingsSwitchItem(
                    icon = Icons.Default.Notifications,
                    title = "Notifications",
                    subtitle = "Manage your alert preferences",
                    checked = settings.notificationsEnabled,
                    onCheckedChange = { viewModel.updateNotifications(it) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                SettingsSwitchItem(
                    icon = Icons.Default.Cloud,
                    title = "Cloud Backup",
                    subtitle = "Keep your data synchronized",
                    checked = settings.backupActive,
                    onCheckedChange = { viewModel.updateBackup(it) }
                )
            }

            // Security Section
            SettingsCategory(title = "Security & Privacy") {
                SettingsSwitchItem(
                    icon = Icons.Default.Fingerprint,
                    title = "Biometric Lock",
                    subtitle = "Secure access with fingerprint",
                    checked = settings.fingerprintEnabled,
                    onCheckedChange = { viewModel.updateFingerprint(it) }
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)
                
                var tempToken by remember { mutableStateOf(settings.privateToken) }
                LaunchedEffect(settings.privateToken) { tempToken = settings.privateToken }

                SettingsInputItem(
                    icon = Icons.Default.VpnKey,
                    title = "Private Token",
                    value = tempToken,
                    onValueChange = { tempToken = it },
                    onSave = { viewModel.updateToken(tempToken) }
                )
            }

            Text(
                text = "Data is encrypted and stored locally.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun SettingsCategory(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(content = content)
        }
    }
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.SemiBold) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(40.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        trailingContent = {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        },
        colors = ListItemDefaults.colors(containerColor = androidx.compose.ui.graphics.Color.Transparent)
    )
}

@Composable
fun SettingsInputItem(
    icon: ImageVector,
    title: String,
    value: String,
    onValueChange: (String) -> Unit,
    onSave: () -> Unit
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = true,
            trailingIcon = {
                IconButton(onClick = onSave) {
                    Icon(Icons.Default.Check, contentDescription = "Save", tint = MaterialTheme.colorScheme.primary)
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                unfocusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}
