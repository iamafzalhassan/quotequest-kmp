package com.afzalhassan.quotequest.core.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Immutable
class AppTextStyles(private val fontFamily: FontFamily) {
    val display = style(size = 32, weight = FontWeight.Bold, color = AppColors.primary, height = 1.2f)
    val screenTitle = style(size = 20, weight = FontWeight.Bold, color = AppColors.textPrimary, height = 1.2f)
    val sectionHeading = style(size = 17, weight = FontWeight.SemiBold, color = AppColors.textPrimary, height = 1.2f)
    val body = style(size = 15, weight = FontWeight.Normal, color = AppColors.textPrimary, height = 1.4f)
    val label = style(size = 14, weight = FontWeight.Normal, color = AppColors.textSecondary, height = 1.3f)
    val button = style(size = 16, weight = FontWeight.SemiBold, color = AppColors.primaryOn, height = 1.2f)
    val fieldLabel = style(size = 12, weight = FontWeight.Medium, color = AppColors.textSecondary, height = 1.3f)
    val fieldValue = style(size = 16, weight = FontWeight.Normal, color = AppColors.textPrimary, height = 1.3f)
    val errorHint = style(size = 12, weight = FontWeight.Medium, color = AppColors.validationError, height = 1.3f)

    val quote = TextStyle(
        color = AppColors.textPrimary,
        fontFamily = FontFamily.Serif,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        lineHeight = 31.sp,
    )

    private fun style(size: Int, weight: FontWeight, color: Color, height: Float) = TextStyle(
        color = color,
        fontFamily = fontFamily,
        fontSize = size.sp,
        fontWeight = weight,
        lineHeight = (size * height).sp,
    )
}