package com.afzalhassan.quotequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.data.LocalStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

private const val SPLASH_MIN_MILLIS = 1_000L

class AppViewModel(localStore: LocalStore) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = localStore.token.map { it != null }.onStart { delay(SPLASH_MIN_MILLIS) }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}