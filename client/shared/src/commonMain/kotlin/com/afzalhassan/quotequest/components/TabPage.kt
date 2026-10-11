package com.afzalhassan.quotequest.components

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
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme

@Composable
fun TabPage(
    title: String,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
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
                if (!isLoading) content()
            }
        }
        if (isLoading) {
            ActivityIndicator(Modifier.align(Alignment.Center).size(AppSpacing.progressIndicatorLarge))
        }
    }
}