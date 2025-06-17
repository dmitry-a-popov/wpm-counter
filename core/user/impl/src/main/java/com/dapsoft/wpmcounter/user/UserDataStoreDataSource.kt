package com.dapsoft.wpmcounter.user

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

internal class UserDataStoreDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.userPreferences

    val userNameFlow: Flow<String?> = dataStore.data.map {
        it[Keys.USER_NAME]
    }

    suspend fun saveUserName(name: String) {
        dataStore.edit {
            it[Keys.USER_NAME] = name
        }
    }

    companion object {
        object Keys {
            val USER_NAME = stringPreferencesKey("user_name")
        }
    }
}