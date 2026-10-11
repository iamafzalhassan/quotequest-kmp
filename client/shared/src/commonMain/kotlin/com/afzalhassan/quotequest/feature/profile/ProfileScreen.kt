package com.afzalhassan.quotequest.feature.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afzalhassan.quotequest.core.ui.components.AppCard
import com.afzalhassan.quotequest.core.ui.components.ErrorState
import com.afzalhassan.quotequest.core.ui.components.SecondaryButton
import com.afzalhassan.quotequest.core.ui.components.TabPage
import com.afzalhassan.quotequest.core.data.ProfileResponse
import com.afzalhassan.quotequest.core.ui.theme.AppColors
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import com.afzalhassan.quotequest.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.profile_logout
import quotequest.shared.generated.resources.tab_profile

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ProfileContent(state = state, onRetry = viewModel::load, onLogout = viewModel::logout)
}

@Composable
private fun ProfileContent(state: ProfileUiState, onRetry: () -> Unit, onLogout: () -> Unit) {
    TabPage(
        title = stringResource(Res.string.tab_profile),
        isLoading = state.profile == null && state.error == null,
    ) {
        when {
            state.profile != null -> ProfileCard(state.profile)
            state.error != null -> ErrorState(message = state.error, onRetry = onRetry)
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