package com.dapsoft.wpmcounter.user.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

import dagger.hilt.android.qualifiers.ApplicationContext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

import javax.inject.Inject

internal val Context.userPreferences: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

/**
 * DataStore data source for persisting lightweight user attributes.
 *
 * Responsibilities:
 * - Exposes a reactive stream for the stored user name.
 * - Provides suspend functions to save and clear the value.
 *
 * Thread safety: DataStore handles its own synchronization; calls here are safe.
 */
internal class UserDataStoreDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.userPreferences

    /**
     * Observe current user name. Emits `null` if not yet set or cleared.
     */
    fun observeUserName(): Flow<String?> =
        dataStore.data
            .map { preferences -> preferences[Keys.USER_NAME] }

    /**
     * Persist a new user name value. Overwrites any existing value.
     */
    suspend fun saveUserName(name: String) {
        dataStore.edit {
            it[Keys.USER_NAME] = name
        }
    }

    /**
     * Remove the stored user name. After this, the flow emits `null`.
     */
    suspend fun clearUserName() {
        dataStore.edit { prefs ->
            prefs.remove(Keys.USER_NAME)
        }
    }

    companion object {
        object Keys {
            val USER_NAME = stringPreferencesKey("user_name")
        }
    }
}
