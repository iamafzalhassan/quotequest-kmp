package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.afzalhassan.quotequest.core.ui.theme.AppColors
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing

@Composable
fun AppCard(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(AppSpacing.radiusCard)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppColors.surfaceCard, shape)
            .border(AppSpacing.hairline, AppColors.divider, shape)
            .padding(AppSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.md),
        horizontalAlignment = horizontalAlignment,
        content = content,
    )
}