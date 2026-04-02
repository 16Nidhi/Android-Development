package datastorepreferencedatastore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val manager: DataStoreManager) : ViewModel() {

    val userName = manager.userNameFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Loading..."
    )

    fun saveName(name: String) {
        viewModelScope.launch {
            manager.saveName(name)
        }
    }
}
