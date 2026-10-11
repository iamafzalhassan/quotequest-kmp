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
import com.afzalhassan.quotequest.login.LoginScreen
import com.afzalhassan.quotequest.main.MainScreen
import com.afzalhassan.quotequest.splash.SplashScreen
import com.afzalhassan.quotequest.theme.QuoteQuestTheme
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    QuoteQuestTheme {
        Surface(Modifier.fillMaxSize()) {
            val viewModel = koinViewModel<AppViewModel>()
            val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()
            val navController = rememberNavController()

            NavHost(navController, startDestination = Route.Splash) {
                composable<Route.Splash> { SplashScreen() }
                composable<Route.Login> { LoginScreen() }
                composable<Route.Main> { MainScreen() }
            }

            LaunchedEffect(isLoggedIn) {
                val target = when (isLoggedIn) {
                    true -> Route.Main
                    false -> Route.Login
                    null -> return@LaunchedEffect
                }
                if (navController.currentDestination?.hasRoute(target::class) != true) {
                    navController.navigate(target) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            }
        }
    }
}