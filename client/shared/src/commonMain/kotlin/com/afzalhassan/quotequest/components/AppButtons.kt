package com.afzalhassan.quotequest.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.afzalhassan.quotequest.theme.AppColors
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme

@Composable
fun PrimaryButton(text: String, onClick: () -> Unit, modifier: Modifier = Modifier, isLoading: Boolean = false) {
    Button(
        onClick = onClick,
        modifier = modifier.heightIn(min = AppSpacing.controlHeight),
        enabled = !isLoading,
        shape = RoundedCornerShape(AppSpacing.radiusButton),
        colors = ButtonDefaults.buttonColors(
            containerColor = AppColors.primary,
            contentColor = AppColors.primaryOn,
            disabledContainerColor = AppColors.primary,
            disabledContentColor = AppColors.primaryOn,
        ),
        contentPadding = PaddingValues(horizontal = AppSpacing.lg),
    ) {
        if (isLoading) {
            ActivityIndicator(color = AppColors.primaryOn)
        } else {
            Text(text, style = AppTheme.textStyles.button)
        }
    }
}

@Composable
fun SecondaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = AppColors.primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
) {
    val activeColor = if (enabled || isLoading) color else AppColors.textDisabled
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.heightIn(min = AppSpacing.controlHeight),
        enabled = enabled && !isLoading,
        shape = RoundedCornerShape(AppSpacing.radiusButton),
        border = BorderStroke(AppSpacing.hairline, if (enabled || isLoading) color else AppColors.divider),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = color, disabledContentColor = activeColor),
        contentPadding = PaddingValues(horizontal = AppSpacing.lg),
    ) {
        if (isLoading) {
            ActivityIndicator(color = color)
        } else {
            Text(text, style = AppTheme.textStyles.button.copy(color = activeColor))
        }
    }
}