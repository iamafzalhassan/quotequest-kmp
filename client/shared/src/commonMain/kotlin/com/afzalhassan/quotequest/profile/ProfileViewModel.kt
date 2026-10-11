package com.afzalhassan.quotequest.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.data.LocalStore
import com.afzalhassan.quotequest.data.QuoteQuestApi
import com.afzalhassan.quotequest.toMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val api: QuoteQuestApi,
    private val localStore: LocalStore,
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileUiState())
    val state: StateFlow<ProfileUiState> = _state.asStateFlow()

    init {
        load()
    }

    fun load() {
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                val profile = api.profile()
                _state.update { it.copy(profile = profile, isLoading = false) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.toMessage()) }
            }
        }
    }

    fun logout() {
        if (_state.value.isLoggingOut) return
        _state.update { it.copy(isLoggingOut = true) }
        viewModelScope.launch {
            try {
                api.logout()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                /* Server unreachable or session already expired: logging out locally is still correct */
            }
            localStore.clearToken()
        }
    }
}