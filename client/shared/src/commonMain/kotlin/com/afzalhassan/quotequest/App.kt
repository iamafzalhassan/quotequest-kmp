package com.afzalhassan.quotequest

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.afzalhassan.quotequest.feature.login.LoginScreen
import com.afzalhassan.quotequest.feature.main.MainScreen
import com.afzalhassan.quotequest.feature.splash.SplashScreen
import com.afzalhassan.quotequest.core.ui.theme.QuoteQuestTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(onReady: () -> Unit = {}) {
    QuoteQuestTheme {
        Surface(Modifier.fillMaxSize()) {
            val viewModel = koinViewModel<AppViewModel>()
            val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
            val navController = rememberNavController()
            val loggedIn = isLoggedIn

            if (loggedIn == null) {
                SplashScreen()
            } else {
                NavHost(navController, startDestination = if (loggedIn) Route.Main else Route.Login) {
                    composable<Route.Login> { LoginScreen() }
                    composable<Route.Main> { MainScreen() }
                }
                LaunchedEffect(loggedIn) {
                    val target = if (loggedIn) Route.Main else Route.Login
                    if (navController.currentDestination?.hasRoute(target::class) != true) {
                        navController.navigate(target) {
                            popUpTo(navController.graph.id) { inclusive = true }
                        }
                    }
                    onReady()
                }
            }
        }
    }
}