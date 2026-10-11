package com.afzalhassan.quotequest

import io.ktor.client.plugins.ResponseException
import org.jetbrains.compose.resources.StringResource
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.error_connection
import quotequest.shared.generated.resources.error_server

fun Exception.toMessage(): StringResource = when (this) {
    is ResponseException -> Res.string.error_server
    else -> Res.string.error_connection
}