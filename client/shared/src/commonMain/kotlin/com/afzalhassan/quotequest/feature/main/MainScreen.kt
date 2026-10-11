package com.afzalhassan.quotequest.feature.main

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.afzalhassan.quotequest.feature.profile.ProfileScreen
import com.afzalhassan.quotequest.feature.quiz.QuizScreen
import com.afzalhassan.quotequest.feature.settings.SettingsScreen
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.ic_profile
import quotequest.shared.generated.resources.ic_quiz
import quotequest.shared.generated.resources.ic_settings
import quotequest.shared.generated.resources.tab_profile
import quotequest.shared.generated.resources.tab_quiz
import quotequest.shared.generated.resources.tab_settings

private enum class MainTab(val label: StringResource, val icon: DrawableResource) {
    Quiz(Res.string.tab_quiz, Res.drawable.ic_quiz),
    Settings(Res.string.tab_settings, Res.drawable.ic_settings),
    Profile(Res.string.tab_profile, Res.drawable.ic_profile),
}

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableStateOf(MainTab.Quiz) }

    Scaffold(
        bottomBar = {
            Column {
                HorizontalDivider()
                NavigationBar {
                    MainTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = tab == selectedTab,
                            onClick = { selectedTab = tab },
                            icon = { Icon(painterResource(tab.icon), contentDescription = null) },
                            label = { Text(stringResource(tab.label)) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Crossfade(targetState = selectedTab, modifier = Modifier.padding(innerPadding), label = "tab") { tab ->
            when (tab) {
                MainTab.Quiz -> QuizScreen()
                MainTab.Settings -> SettingsScreen()
                MainTab.Profile -> ProfileScreen()
            }
        }
    }
}