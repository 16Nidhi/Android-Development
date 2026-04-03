package datastorepreferencedatastore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
fun SecureSettingsPage(viewModel: SecureSettingsViewModel) {
    val settings by viewModel.uiState.collectAsState()
    
    // Apply theme based on setting
    MaterialTheme(
        colorScheme = if (settings.isDarkMode) darkColorScheme() else lightColorScheme()
    ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Secure Settings", fontWeight = FontWeight.Bold) },
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
                    SectionTitle(title = "Appearance")
                    PreferenceSwitch(
                        label = "Dark Mode",
                        checked = settings.isDarkMode,
                        onCheckedChange = { viewModel.updateDarkMode(it) }
                    )

                    HorizontalDivider()

                    // Profile Section
                    SectionTitle(title = "Profile")
                    var tempUsername by remember { mutableStateOf(settings.username) }
                    LaunchedEffect(settings.username) { tempUsername = settings.username }
                    
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

                    // Preferences Section
                    SectionTitle(title = "Preferences")
                    PreferenceSwitch(
                        label = "Enable Notifications",
                        checked = settings.notificationsEnabled,
                        onCheckedChange = { viewModel.updateNotifications(it) }
                    )
                    PreferenceSwitch(
                        label = "Cloud Backup Activation",
                        checked = settings.backupActive,
                        onCheckedChange = { viewModel.updateBackup(it) }
                    )

                    HorizontalDivider()

                    // Security Section
                    SectionTitle(title = "Security & Privacy")
                    PreferenceSwitch(
                        label = "Fingerprint Unlock",
                        checked = settings.fingerprintEnabled,
                        onCheckedChange = { viewModel.updateFingerprint(it) }
                    )
                    
                    var tempToken by remember { mutableStateOf(settings.privateToken) }
                    LaunchedEffect(settings.privateToken) { tempToken = settings.privateToken }

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
                        text = "Keys are persisted securely in Preferences DataStore.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun PreferenceSwitch(label: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 16.sp)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
