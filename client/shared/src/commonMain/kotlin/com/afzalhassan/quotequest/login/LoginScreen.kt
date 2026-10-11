package com.afzalhassan.quotequest.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afzalhassan.quotequest.components.AppTextField
import com.afzalhassan.quotequest.components.PrimaryButton
import com.afzalhassan.quotequest.theme.AppColors
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.login_button
import quotequest.shared.generated.resources.login_email_hint
import quotequest.shared.generated.resources.login_email_label
import quotequest.shared.generated.resources.login_password_hide
import quotequest.shared.generated.resources.login_password_hint
import quotequest.shared.generated.resources.login_password_label
import quotequest.shared.generated.resources.login_password_show
import quotequest.shared.generated.resources.login_subtitle
import quotequest.shared.generated.resources.login_title

@Composable
fun LoginScreen(viewModel: LoginViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    LoginContent(
        state = state,
        onEmailChange = viewModel::onEmailChange,
        onPasswordChange = viewModel::onPasswordChange,
        onLogin = viewModel::login,
    )
}

@Composable
private fun LoginContent(
    state: LoginUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val submit = {
        focusManager.clearFocus()
        onLogin()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(AppSpacing.screenPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.widthIn(max = AppSpacing.contentMaxWidth).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        ) {
            Text(stringResource(Res.string.login_title), style = AppTheme.textStyles.display)
            Text(
                text = stringResource(Res.string.login_subtitle),
                style = AppTheme.textStyles.label,
                modifier = Modifier.padding(bottom = AppSpacing.lg),
            )
            AppTextField(
                value = state.email,
                onValueChange = onEmailChange,
                label = stringResource(Res.string.login_email_label),
                hint = stringResource(Res.string.login_email_hint),
                error = state.emailError?.let { stringResource(it) },
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next,
                ),
            )
            AppTextField(
                value = state.password,
                onValueChange = onPasswordChange,
                label = stringResource(Res.string.login_password_label),
                hint = stringResource(Res.string.login_password_hint),
                error = state.passwordError?.let { stringResource(it) },
                enabled = !state.isLoading,
                keyboardOptions = KeyboardOptions(
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(onDone = { submit() }),
                visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailing = {
                    TextButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                        Text(
                            stringResource(if (isPasswordVisible) Res.string.login_password_hide else Res.string.login_password_show),
                        )
                    }
                },
            )
            state.formError?.let {
                Text(
                    text = stringResource(it),
                    style = AppTheme.textStyles.label.copy(color = AppColors.danger),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            PrimaryButton(
                text = stringResource(Res.string.login_button),
                onClick = submit,
                isLoading = state.isLoading,
                modifier = Modifier.fillMaxWidth().padding(top = AppSpacing.sm),
            )
        }
    }
}