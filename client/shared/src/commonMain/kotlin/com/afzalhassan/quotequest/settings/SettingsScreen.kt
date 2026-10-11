package com.afzalhassan.quotequest.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.afzalhassan.quotequest.components.AppCard
import com.afzalhassan.quotequest.components.ChoiceRow
import com.afzalhassan.quotequest.components.TabPage
import com.afzalhassan.quotequest.data.QuizMode
import com.afzalhassan.quotequest.theme.AppSpacing
import com.afzalhassan.quotequest.theme.AppTheme
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.mode_binary
import quotequest.shared.generated.resources.mode_binary_description
import quotequest.shared.generated.resources.mode_multiple
import quotequest.shared.generated.resources.mode_multiple_description
import quotequest.shared.generated.resources.settings_mode_restart_note
import quotequest.shared.generated.resources.settings_mode_title
import quotequest.shared.generated.resources.tab_settings

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = koinViewModel()) {
    val mode by viewModel.mode.collectAsStateWithLifecycle()
    SettingsContent(mode = mode, onModeSelected = viewModel::onModeSelected)
}

@Composable
private fun SettingsContent(mode: QuizMode, onModeSelected: (QuizMode) -> Unit) {
    TabPage(title = stringResource(Res.string.tab_settings)) {
        AppCard {
            Text(stringResource(Res.string.settings_mode_title), style = AppTheme.textStyles.sectionHeading)
            ChoiceRow(
                options = QuizMode.entries,
                selected = mode,
                onSelect = onModeSelected,
                label = { stringResource(it.label) },
            )
            Text(stringResource(mode.description), style = AppTheme.textStyles.label)
        }
        Text(
            text = stringResource(Res.string.settings_mode_restart_note),
            style = AppTheme.textStyles.label,
            modifier = Modifier.padding(horizontal = AppSpacing.xs),
        )
    }
}

private val QuizMode.label: StringResource
    get() = when (this) {
        QuizMode.BINARY -> Res.string.mode_binary
        QuizMode.MULTIPLE -> Res.string.mode_multiple
    }

private val QuizMode.description: StringResource
    get() = when (this) {
        QuizMode.BINARY -> Res.string.mode_binary_description
        QuizMode.MULTIPLE -> Res.string.mode_multiple_description
    }