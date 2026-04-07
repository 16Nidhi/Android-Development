package datastorepreferencedatastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
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
    val privateToken: String = "",
    val notificationVolume: Float = 0.5f,
    val lastBackupDate: String = "Never",
    val securityLevel: String = "Standard",
    val themeColor: String = "Indigo"
)

class SecureSettingsRepository(private val context: Context) {
    private object Keys {
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val BACKUP = booleanPreferencesKey("backup_active")
        val USERNAME = stringPreferencesKey("username")
        val FINGERPRINT = booleanPreferencesKey("fingerprint_enabled")
        val PRIVATE_TOKEN = stringPreferencesKey("private_token")
        val NOTIFICATION_VOLUME = floatPreferencesKey("notification_volume")
        val LAST_BACKUP_DATE = stringPreferencesKey("last_backup_date")
        val SECURITY_LEVEL = stringPreferencesKey("security_level")
        val THEME_COLOR = stringPreferencesKey("theme_color")
    }

    val settingsFlow: Flow<AppSettings> = context.secureSettingsStore.data.map { prefs ->
        AppSettings(
            isDarkMode = prefs[Keys.DARK_MODE] ?: false,
            notificationsEnabled = prefs[Keys.NOTIFICATIONS] ?: true,
            backupActive = prefs[Keys.BACKUP] ?: false,
            username = prefs[Keys.USERNAME] ?: "User",
            fingerprintEnabled = prefs[Keys.FINGERPRINT] ?: false,
            privateToken = prefs[Keys.PRIVATE_TOKEN] ?: "",
            notificationVolume = prefs[Keys.NOTIFICATION_VOLUME] ?: 0.5f,
            lastBackupDate = prefs[Keys.LAST_BACKUP_DATE] ?: "Never",
            securityLevel = prefs[Keys.SECURITY_LEVEL] ?: "Standard",
            themeColor = prefs[Keys.THEME_COLOR] ?: "Indigo"
        )
    }

    suspend fun updateDarkMode(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.DARK_MODE] = enabled }
    suspend fun updateNotifications(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.NOTIFICATIONS] = enabled }
    suspend fun updateBackup(active: Boolean) = context.secureSettingsStore.edit { it[Keys.BACKUP] = active }
    suspend fun updateUsername(name: String) = context.secureSettingsStore.edit { it[Keys.USERNAME] = name }
    suspend fun updateFingerprint(enabled: Boolean) = context.secureSettingsStore.edit { it[Keys.FINGERPRINT] = enabled }
    suspend fun updatePrivateToken(token: String) = context.secureSettingsStore.edit { it[Keys.PRIVATE_TOKEN] = token }
    suspend fun updateNotificationVolume(volume: Float) = context.secureSettingsStore.edit { it[Keys.NOTIFICATION_VOLUME] = volume }
    suspend fun updateLastBackupDate(date: String) = context.secureSettingsStore.edit { it[Keys.LAST_BACKUP_DATE] = date }
    suspend fun updateSecurityLevel(level: String) = context.secureSettingsStore.edit { it[Keys.SECURITY_LEVEL] = level }
    suspend fun updateThemeColor(color: String) = context.secureSettingsStore.edit { it[Keys.THEME_COLOR] = color }

    suspend fun clearSettings() = context.secureSettingsStore.edit { it.clear() }
}
