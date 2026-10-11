package com.afzalhassan.quotequest.core.ui.theme

import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.inter_bold
import quotequest.shared.generated.resources.inter_medium
import quotequest.shared.generated.resources.inter_regular
import quotequest.shared.generated.resources.inter_semibold

private val ColorScheme = lightColorScheme(
    primary = AppColors.primary,
    onPrimary = AppColors.primaryOn,
    secondaryContainer = AppColors.surfaceField,
    onSecondaryContainer = AppColors.primary,
    background = AppColors.surfaceBase,
    onBackground = AppColors.textPrimary,
    surface = AppColors.surfaceBase,
    onSurface = AppColors.textPrimary,
    onSurfaceVariant = AppColors.textSecondary,
    surfaceContainer = AppColors.surfaceCard,
    surfaceContainerHigh = AppColors.surfaceCard,
    error = AppColors.danger,
    outline = AppColors.divider,
    outlineVariant = AppColors.divider,
)

private val LocalAppTextStyles = staticCompositionLocalOf<AppTextStyles> {
    error("AppTextStyles are provided by QuoteQuestTheme")
}

@Composable
fun QuoteQuestTheme(content: @Composable () -> Unit) {
    val inter = FontFamily(
        Font(Res.font.inter_regular, FontWeight.Normal),
        Font(Res.font.inter_medium, FontWeight.Medium),
        Font(Res.font.inter_semibold, FontWeight.SemiBold),
        Font(Res.font.inter_bold, FontWeight.Bold),
    )
    val textStyles = remember(inter) { AppTextStyles(inter) }
    val typography = remember(textStyles) { textStyles.toTypography() }
    val selectionColors = TextSelectionColors(
        handleColor = AppColors.primary,
        backgroundColor = AppColors.primary.copy(alpha = 0.24f),
    )

    CompositionLocalProvider(
        LocalAppTextStyles provides textStyles,
        LocalTextSelectionColors provides selectionColors,
    ) {
        MaterialTheme(colorScheme = ColorScheme, typography = typography, content = content)
    }
}

object AppTheme {
    val textStyles: AppTextStyles
        @Composable
        @ReadOnlyComposable
        get() = LocalAppTextStyles.current
}

private fun AppTextStyles.toTypography() = Typography(
    headlineSmall = screenTitle.withoutColor(),
    titleLarge = screenTitle.withoutColor(),
    titleMedium = sectionHeading.withoutColor(),
    bodyLarge = body.withoutColor(),
    bodyMedium = body.withoutColor(),
    labelLarge = button.withoutColor(),
    labelMedium = fieldLabel.withoutColor(),
)

private fun TextStyle.withoutColor() = copy(color = Color.Unspecified)