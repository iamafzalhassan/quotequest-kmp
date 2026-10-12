package com.afzalhassan.quotequest

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.afzalhassan.quotequest.core.di.initKoin
import java.awt.Dimension
import java.awt.GraphicsEnvironment

fun main() {
    initKoin(serverUrl = System.getProperty("quotequest.serverUrl") ?: "http://localhost:8080")
    val screen = GraphicsEnvironment.getLocalGraphicsEnvironment().maximumWindowBounds
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "QuoteQuest",
            icon = appIcon(),
            state = rememberWindowState(
                position = WindowPosition(Alignment.Center),
                width = 400.dp,
                height = minOf(800, screen.height).dp,
            ),
        ) {
            LaunchedEffect(Unit) { window.minimumSize = Dimension(360, 480) }
            App()
        }
    }
}