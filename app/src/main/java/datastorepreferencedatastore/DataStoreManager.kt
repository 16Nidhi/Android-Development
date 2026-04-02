package datastorepreferencedatastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

    suspend fun saveName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.USER_NAME] = name
        }
    }

    val userNameFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[Keys.USER_NAME] ?: "No Name"
    }
}
