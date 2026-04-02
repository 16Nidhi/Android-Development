package datastorepreferencedatastore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.ViewModelProvider

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val manager = DataStoreManager(this)
        val factory = MainViewModelFactory(manager)
        val viewModel = ViewModelProvider(this, factory)
            .get(MainViewModel::class.java)

        setContent {
            MaterialTheme {
                MainScreen(viewModel)
            }
        }
    }
}
