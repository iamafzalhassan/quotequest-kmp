package com.afzalhassan.quotequest.feature.login

import org.jetbrains.compose.resources.StringResource

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val emailError: StringResource? = null,
    val passwordError: StringResource? = null,
    val formError: StringResource? = null,
    val isLoading: Boolean = false,
)