package com.afzalhassan.quotequest.feature.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.core.data.LocalStore
import com.afzalhassan.quotequest.core.data.LoginRequest
import com.afzalhassan.quotequest.core.data.QuoteQuestApi
import com.afzalhassan.quotequest.core.ui.toMessage
import io.ktor.client.plugins.ClientRequestException
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.error_email_required
import quotequest.shared.generated.resources.error_invalid_credentials
import quotequest.shared.generated.resources.error_password_required

class LoginViewModel(
    private val api: QuoteQuestApi,
    private val localStore: LocalStore,
) : ViewModel() {

    private val _state = MutableStateFlow(LoginUiState())
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    fun onEmailChange(email: String) {
        _state.update { it.copy(email = email, emailError = null, formError = null) }
    }

    fun onPasswordChange(password: String) {
        _state.update { it.copy(password = password, passwordError = null, formError = null) }
    }

    fun login() {
        val current = _state.value
        if (current.isLoading) return

        val emailError = if (current.email.isBlank()) Res.string.error_email_required else null
        val passwordError =
            if (current.password.isEmpty()) Res.string.error_password_required else null
        if (emailError != null || passwordError != null) {
            _state.update { it.copy(emailError = emailError, passwordError = passwordError) }
            return
        }

        _state.update { it.copy(isLoading = true, formError = null) }
        viewModelScope.launch {
            try {
                val response = api.login(LoginRequest(current.email.trim(), current.password))
                localStore.saveToken(response.token)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, formError = e.toLoginMessage()) }
            }
        }
    }
}

private fun Exception.toLoginMessage(): StringResource =
    if (this is ClientRequestException && response.status == HttpStatusCode.Unauthorized) {
        Res.string.error_invalid_credentials
    } else {
        toMessage()
    }