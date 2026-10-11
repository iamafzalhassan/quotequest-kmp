package com.afzalhassan.quotequest.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.semantics.error
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.VisualTransformation
import com.afzalhassan.quotequest.theme.AppColors
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    hint: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailing: (@Composable () -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    val shape = RoundedCornerShape(AppSpacing.radiusField)
    val borderColor = when {
        error != null -> AppColors.validationError
        isFocused -> AppColors.primary
        else -> Color.Transparent
    }

    Column(modifier) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .semantics { if (error != null) error(error) },
            enabled = enabled,
            textStyle = AppTheme.textStyles.fieldValue,
            keyboardOptions = keyboardOptions,
            keyboardActions = keyboardActions,
            singleLine = true,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            cursorBrush = SolidColor(AppColors.primary),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = AppSpacing.controlHeight)
                        .background(AppColors.surfaceField, shape)
                        .border(AppSpacing.hairline, borderColor, shape)
                        .padding(start = AppSpacing.md, end = if (trailing == null) AppSpacing.md else AppSpacing.xs),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f).padding(vertical = AppSpacing.sm)) {
                        Text(
                            text = label,
                            style = AppTheme.textStyles.fieldLabel.copy(
                                color = if (isFocused) AppColors.primary else AppColors.textSecondary,
                            ),
                        )
                        Box {
                            if (value.isEmpty()) {
                                Text(
                                    hint,
                                    style = AppTheme.textStyles.fieldValue.copy(color = AppColors.textDisabled)
                                )
                            }
                            innerTextField()
                        }
                    }
                    trailing?.invoke()
                }
            },
        )
        if (error != null) {
            Text(
                text = error,
                style = AppTheme.textStyles.errorHint,
                modifier = Modifier.padding(start = AppSpacing.md, top = AppSpacing.xs),
            )
        }
    }
}