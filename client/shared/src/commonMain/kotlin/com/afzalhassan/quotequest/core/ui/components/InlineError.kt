package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import com.afzalhassan.quotequest.core.ui.theme.AppColors
import com.afzalhassan.quotequest.core.ui.theme.AppTheme

@Composable
fun InlineError(message: String, modifier: Modifier = Modifier) {
    Text(
        text = message,
        style = AppTheme.textStyles.label.copy(color = AppColors.validationError),
        modifier = modifier
            .fillMaxWidth()
            .semantics { liveRegion = LiveRegionMode.Polite },
    )
}