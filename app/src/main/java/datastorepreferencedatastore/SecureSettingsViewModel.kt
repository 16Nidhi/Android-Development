package datastorepreferencedatastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SecureSettingsViewModel(private val repository: SecureSettingsRepository) : ViewModel() {

    val uiState: StateFlow<AppSettings> = repository.settingsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppSettings()
    )

    fun updateDarkMode(enabled: Boolean) = viewModelScope.launch { repository.updateDarkMode(enabled) }
    fun updateNotifications(enabled: Boolean) = viewModelScope.launch { repository.updateNotifications(enabled) }
    fun updateBackup(active: Boolean) = viewModelScope.launch { repository.updateBackup(active) }
    fun updateUsername(name: String) = viewModelScope.launch { repository.updateUsername(name) }
    fun updateFingerprint(enabled: Boolean) = viewModelScope.launch { repository.updateFingerprint(enabled) }
    fun updateToken(token: String) = viewModelScope.launch { repository.updatePrivateToken(token) }
    
    fun updateNotificationVolume(volume: Float) = viewModelScope.launch {
        repository.updateNotificationVolume(volume)
    }

    fun triggerManualBackup() = viewModelScope.launch {
        val currentDate = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date())
        repository.updateLastBackupDate(currentDate)
        repository.updateBackup(true)
    }

    fun updateSecurityLevel(level: String) = viewModelScope.launch {
        repository.updateSecurityLevel(level)
    }

    fun clearAllSettings() = viewModelScope.launch {
        repository.clearSettings()
    }
}

class SecureSettingsViewModelFactory(private val repository: SecureSettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SecureSettingsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SecureSettingsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
