package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import com.afzalhassan.quotequest.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.StringResource

@Composable
fun TabPage(
    title: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    error: StringResource? = null,
    onRetry: () -> Unit = {},
    errorActions: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(AppSpacing.screenPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = AppSpacing.contentMaxWidth).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(AppSpacing.lg),
            ) {
                Text(title, style = AppTheme.textStyles.screenTitle)
                if (!isLoading && error == null) content()
            }
        }
        when {
            isLoading -> ActivityIndicator(Modifier.align(Alignment.Center).size(AppSpacing.progressIndicatorLarge))
            error != null -> ErrorState(
                message = error,
                onRetry = onRetry,
                actions = errorActions,
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = AppSpacing.contentMaxWidth)
                    .padding(AppSpacing.screenPadding),
            )
        }
    }
}