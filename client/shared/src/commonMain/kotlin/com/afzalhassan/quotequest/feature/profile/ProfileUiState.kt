package com.afzalhassan.quotequest.feature.profile

import com.afzalhassan.quotequest.core.data.ProfileResponse
import org.jetbrains.compose.resources.StringResource

data class ProfileUiState(
    val profile: ProfileResponse? = null,
    val isLoading: Boolean = true,
    val error: StringResource? = null,
    val isLoggingOut: Boolean = false,
)