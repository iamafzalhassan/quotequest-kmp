package com.afzalhassan.quotequest.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afzalhassan.quotequest.components.ActivityIndicator
import com.afzalhassan.quotequest.components.AppCard
import com.afzalhassan.quotequest.components.SecondaryButton
import com.afzalhassan.quotequest.components.TabPage
import com.afzalhassan.quotequest.data.ProfileResponse
import com.afzalhassan.quotequest.theme.AppColors
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.action_retry
import quotequest.shared.generated.resources.profile_logout
import quotequest.shared.generated.resources.tab_profile

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileContent(state = state, onRetry = viewModel::load, onLogout = viewModel::logout)
}

@Composable
private fun ProfileContent(state: ProfileUiState, onRetry: () -> Unit, onLogout: () -> Unit) {
    TabPage(title = stringResource(Res.string.tab_profile)) {
        when {
            state.profile != null -> ProfileCard(state.profile)
            state.error != null -> {
                Text(
                    text = stringResource(state.error),
                    style = AppTheme.textStyles.body,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                SecondaryButton(
                    text = stringResource(Res.string.action_retry),
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            else -> ActivityIndicator(Modifier.align(Alignment.CenterHorizontally).padding(AppSpacing.xl))
        }
        SecondaryButton(
            text = stringResource(Res.string.profile_logout),
            onClick = onLogout,
            color = AppColors.danger,
            isLoading = state.isLoggingOut,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ProfileCard(profile: ProfileResponse) {
    AppCard(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier.size(AppSpacing.avatar).background(AppColors.primary, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(profile.name.initials(), style = AppTheme.textStyles.screenTitle.copy(color = AppColors.primaryOn))
        }
        Text(profile.name, style = AppTheme.textStyles.sectionHeading)
        Text(profile.email, style = AppTheme.textStyles.label)
    }
}

private fun String.initials(): String = split(" ").filter { it.isNotBlank() }.take(2).joinToString("") { it.first().uppercase() }