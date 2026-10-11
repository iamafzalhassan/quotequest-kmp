package com.afzalhassan.quotequest

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.afzalhassan.quotequest.di.initKoin
import java.awt.Dimension

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "QuoteQuest",
            state = rememberWindowState(width = 400.dp, height = 800.dp),
        ) {
            LaunchedEffect(Unit) { window.minimumSize = Dimension(360, 480) }
            App()
        }
    }
}