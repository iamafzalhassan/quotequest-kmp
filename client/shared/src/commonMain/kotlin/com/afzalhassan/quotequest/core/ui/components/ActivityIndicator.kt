package com.afzalhassan.quotequest.core.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.progressSemantics
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import com.afzalhassan.quotequest.core.ui.theme.AppColors
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import kotlin.math.floor

private const val PERIOD_MILLIS = 1000
private const val TICK_COUNT = 8
private const val TICK_LENGTH_RATIO = 2f / 3f
private const val TICK_WIDTH_RATIO = 0.2f
private val tickAlphas = floatArrayOf(0.18f, 0.18f, 0.18f, 0.18f, 0.28f, 0.38f, 0.48f, 0.58f)

@Composable
fun ActivityIndicator(modifier: Modifier = Modifier, color: Color = AppColors.textPrimary) {
    val position by rememberInfiniteTransition(label = "activityIndicator").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(PERIOD_MILLIS, easing = LinearEasing)),
        label = "activityIndicatorPosition",
    )

    Canvas(modifier.progressSemantics().size(AppSpacing.progressIndicator)) {
        val radius = size.minDimension / 2f
        val tickWidth = radius * TICK_WIDTH_RATIO
        val activeTick = floor(TICK_COUNT * position).toInt()

        repeat(TICK_COUNT) { index ->
            rotate(degrees = 360f / TICK_COUNT * index) {
                drawRoundRect(
                    color = color.copy(alpha = tickAlphas[(index - activeTick).mod(TICK_COUNT)]),
                    topLeft = Offset(center.x - tickWidth / 2f, center.y - radius),
                    size = Size(tickWidth, radius * TICK_LENGTH_RATIO),
                    cornerRadius = CornerRadius(tickWidth / 2f),
                )
            }
        }
    }
}