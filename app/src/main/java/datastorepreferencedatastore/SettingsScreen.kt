package datastorepreferencedatastore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val settings by viewModel.settings.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Secure Settings") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Appearance Section
            SettingsSectionTitle(title = "Appearance")
            SettingsSwitchItem(
                label = "Dark Mode",
                checked = settings.darkMode,
                onCheckedChange = { viewModel.toggleDarkMode(it) }
            )

            HorizontalDivider()

            // Profile Section
            SettingsSectionTitle(title = "Profile")
            var tempUsername by remember { mutableStateOf(settings.username) }
            LaunchedEffect(settings.username) {
                tempUsername = settings.username
            }
            
            OutlinedTextField(
                value = tempUsername,
                onValueChange = { tempUsername = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    TextButton(onClick = { viewModel.updateUsername(tempUsername) }) {
                        Text("Save")
                    }
                }
            )

            HorizontalDivider()

            // Notifications & Backup
            SettingsSectionTitle(title = "Preferences")
            SettingsSwitchItem(
                label = "Enable Notifications",
                checked = settings.notificationsEnabled,
                onCheckedChange = { viewModel.toggleNotifications(it) }
            )
            SettingsSwitchItem(
                label = "Cloud Backup Activation",
                checked = settings.backupActive,
                onCheckedChange = { viewModel.toggleBackup(it) }
            )

            HorizontalDivider()

            // Security Section
            SettingsSectionTitle(title = "Security & Privacy")
            SettingsSwitchItem(
                label = "Fingerprint Unlock",
                checked = settings.fingerprintEnabled,
                onCheckedChange = { viewModel.toggleFingerprint(it) }
            )
            
            var tempToken by remember { mutableStateOf(settings.secureToken) }
            LaunchedEffect(settings.secureToken) {
                tempToken = settings.secureToken
            }

            OutlinedTextField(
                value = tempToken,
                onValueChange = { tempToken = it },
                label = { Text("Private Access Token") },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Enter secure token") },
                trailingIcon = {
                    IconButton(onClick = { viewModel.updateToken(tempToken) }) {
                        Icon(Icons.Default.Security, contentDescription = "Secure Save")
                    }
                }
            )
            Text(
                text = "Keys are persisted securely in DataStore.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun SettingsSwitchItem(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
