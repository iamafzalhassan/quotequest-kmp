package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextOverflow
import com.afzalhassan.quotequest.core.ui.theme.AppColors
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import com.afzalhassan.quotequest.core.ui.theme.AppTheme

@Composable
fun <T> ChoiceRow(
    options: List<T>,
    selected: T,
    onSelect: (T) -> Unit,
    label: @Composable (T) -> String,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(AppSpacing.radiusButton)

    Row(
        modifier = modifier.fillMaxWidth().selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(AppSpacing.sm),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            val background by animateColorAsState(if (isSelected) AppColors.primary else AppColors.surfaceCard, label = "choiceBackground")
            val content by animateColorAsState(if (isSelected) AppColors.primaryOn else AppColors.textPrimary, label = "choiceContent")

            Box(
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = AppSpacing.controlHeight)
                    .clip(shape)
                    .background(background, shape)
                    .border(AppSpacing.hairline, if (isSelected) AppColors.primary else AppColors.divider, shape)
                    .selectable(selected = isSelected, onClick = { onSelect(option) }, role = Role.RadioButton)
                    .padding(horizontal = AppSpacing.sm),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    style = AppTheme.textStyles.button.copy(color = content),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}