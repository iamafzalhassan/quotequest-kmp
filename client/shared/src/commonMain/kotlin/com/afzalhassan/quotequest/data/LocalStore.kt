package com.afzalhassan.quotequest.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import okio.Path.Companion.toPath

class LocalStore(private val dataStore: DataStore<Preferences>) {

    val token: Flow<String?> = dataStore.data.map { it[TOKEN] }.distinctUntilChanged()

    val quizMode: Flow<QuizMode> = dataStore.data.map { prefs -> QuizMode.entries.find { it.name == prefs[QUIZ_MODE] } ?: QuizMode.BINARY }.distinctUntilChanged()

    suspend fun saveToken(token: String) {
        dataStore.edit { it[TOKEN] = token }
    }

    suspend fun clearToken() {
        dataStore.edit { it.remove(TOKEN) }
    }

    suspend fun saveQuizMode(mode: QuizMode) {
        dataStore.edit { it[QUIZ_MODE] = mode.name }
    }

    private companion object {
        val TOKEN = stringPreferencesKey("token")
        val QUIZ_MODE = stringPreferencesKey("quiz_mode")
    }
}

internal const val DATA_STORE_FILE = "quotequest.preferences_pb"

internal fun createDataStore(path: String): DataStore<Preferences> = PreferenceDataStoreFactory.createWithPath(produceFile = { path.toPath() })