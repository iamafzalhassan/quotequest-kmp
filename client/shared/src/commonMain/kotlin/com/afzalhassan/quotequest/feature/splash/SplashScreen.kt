package com.afzalhassan.quotequest.feature.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.afzalhassan.quotequest.core.ui.components.ActivityIndicator
import com.afzalhassan.quotequest.core.ui.theme.AppSpacing
import com.afzalhassan.quotequest.core.ui.theme.AppTheme
import org.jetbrains.compose.resources.stringResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.app_name
import quotequest.shared.generated.resources.splash_tagline

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier.fillMaxSize().padding(AppSpacing.screenPadding),
        verticalArrangement = Arrangement.spacedBy(AppSpacing.xl, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(stringResource(Res.string.app_name), style = AppTheme.textStyles.display)
        Text(stringResource(Res.string.splash_tagline), style = AppTheme.textStyles.quote)
        ActivityIndicator()
    }
}