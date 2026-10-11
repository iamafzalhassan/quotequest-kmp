package com.afzalhassan.quotequest.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.core.data.LocalStore
import com.afzalhassan.quotequest.core.data.QuizMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val localStore: LocalStore) : ViewModel() {

    val mode: StateFlow<QuizMode> = localStore.quizMode.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), QuizMode.BINARY)

    fun onModeSelected(mode: QuizMode) {
        viewModelScope.launch { localStore.saveQuizMode(mode) }
    }
}