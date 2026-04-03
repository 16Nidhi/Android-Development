package datastorepreferencedatastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore delegate
val Context.secureSettingsStore by preferencesDataStore(name = "secure_user_prefs")

data class AppSettings(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val backupActive: Boolean = false,
    val username: String = "User",
    val fingerprintEnabled: Boolean = false,
    val privateToken: String = ""
)

class SecureSettingsRepository(private val context: Context) {
    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val BACKUP = booleanPreferencesKey("backup_active")
        val USERNAME = stringPreferencesKey("username")
        val FINGERPRINT = booleanPreferencesKey("fingerprint_enabled")
        val PRIVATE_TOKEN = stringPreferencesKey("private_token")
    }

    val settingsFlow: Flow<AppSettings> = context.secureSettingsStore.data.map { prefs ->
        AppSettings(
            isDarkMode = prefs[Keys.DARK_MODE] ?: false,
            notificationsEnabled = prefs[Keys.NOTIFICATIONS] ?: true,
            backupActive = prefs[Keys.BACKUP] ?: false,
            username = prefs[Keys.USERNAME] ?: "User",
            fingerprintEnabled = prefs[Keys.FINGERPRINT] ?: false,
            privateToken = prefs[Keys.PRIVATE_TOKEN] ?: ""
        )
    }

    suspend fun updateDarkMode(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.DARK_MODE] = enabled }
    suspend fun updateNotifications(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    suspend fun updateBackup(active: Boolean) = context.secureSettingsStore.edit { it[Keys.BACKUP] = active }
    suspend fun updateUsername(name: String) = context.secureSettingsStore.edit { it[Keys.USERNAME] = name }
    suspend fun updateFingerprint(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.FINGERPRINT] = enabled }
    suspend fun updatePrivateToken(token: String) = context.secureSettingsStore.edit { it[Keys.PRIVATE_TOKEN] = token }
}
