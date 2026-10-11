package com.afzalhassan.quotequest.core.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class LocalStore(private val dataStore: DataStore<Preferences>) {

    val token: Flow<String?> = dataStore.data.map { it[TOKEN] }.distinctUntilChanged()

    val isSessionExpired: Flow<Boolean> = dataStore.data.map { it[SESSION_EXPIRED] == true }.distinctUntilChanged()

    val quizMode: Flow<QuizMode> = dataStore.data.map { prefs ->
        val stored = prefs[QUIZ_MODE]
        QuizMode.entries.find { it.name == stored } ?: QuizMode.BINARY
    }.distinctUntilChanged()

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN] = token
            it.remove(SESSION_EXPIRED)
        }
    }

    suspend fun clearToken() {
        dataStore.edit {
            it.remove(TOKEN)
            it.remove(SESSION_EXPIRED)
        }
    }

    suspend fun expireSession() {
        dataStore.edit {
            if (TOKEN in it) {
                it.remove(TOKEN)
                it[SESSION_EXPIRED] = true
            }
        }
    }

    suspend fun saveQuizMode(mode: QuizMode) {
        dataStore.edit { it[QUIZ_MODE] = mode.name }
    }

    private companion object {
        val TOKEN = stringPreferencesKey("token")
        val QUIZ_MODE = stringPreferencesKey("quiz_mode")
        val SESSION_EXPIRED = booleanPreferencesKey("session_expired")
    }
}

internal const val DATA_STORE_FILE = "quotequest.preferences_pb"

internal fun createDataStore(path: String): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(produceFile = { path.toPath() })