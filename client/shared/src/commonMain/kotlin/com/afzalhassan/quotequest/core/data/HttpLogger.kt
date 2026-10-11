package com.afzalhassan.quotequest.core.data

import io.ktor.client.plugins.logging.Logger

private const val BORDER = "──────────────────────────────────────────────"

internal object HttpLogger : Logger {
    override fun log(message: String) {
        println(
            message.lines().joinToString(separator = "\n", prefix = "┌$BORDER\n", postfix = "\n└$BORDER") { "│ $it" },
        )
    }
}