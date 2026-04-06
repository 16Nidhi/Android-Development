package datastorepreferencedatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Initialize Repository and ViewModel for Secure Settings
        val repository = SecureSettingsRepository(applicationContext)
        val factory = SecureSettingsViewModelFactory(repository)
        val viewModel = ViewModelProvider(this, factory)[SecureSettingsViewModel::class.java]

        setContent {
            // Updated to use SettingsScreen which contains the modern UI and resolved warnings
            SettingsScreen(viewModel)
        }
    }
}
