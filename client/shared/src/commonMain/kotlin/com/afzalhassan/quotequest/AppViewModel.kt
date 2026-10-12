package com.afzalhassan.quotequest

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.core.data.LocalStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class AppViewModel(localStore: LocalStore) : ViewModel() {

    val isLoggedIn: StateFlow<Boolean?> = localStore.token.map { it != null }.onStart { delay(splashMinMillis) }.stateIn(viewModelScope, SharingStarted.Eagerly, null)
}