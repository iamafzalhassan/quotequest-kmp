package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import com.afzalhassan.quotequest.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.action_retry

@Composable
fun ErrorState(
    message: StringResource,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    actions: @Composable ColumnScope.() -> Unit = {},
) {
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(AppSpacing.lg)) {
        Text(
            text = stringResource(message),
            style = AppTheme.textStyles.body,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        SecondaryButton(text = stringResource(Res.string.action_retry), onClick = onRetry, modifier = Modifier.fillMaxWidth())
        actions()
    }
}