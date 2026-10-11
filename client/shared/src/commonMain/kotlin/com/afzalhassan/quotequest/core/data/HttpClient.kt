package com.afzalhassan.quotequest.core.data

import io.ktor.client.HttpClient
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.api.createClientPlugin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.LoggingFormat
import io.ktor.client.request.bearerAuth
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

internal fun createHttpClient(
    serverUrl: String,
    localStore: LocalStore,
    isLoggingEnabled: Boolean,
) = HttpClient {
    expectSuccess = true
    install(HttpTimeout) {
        requestTimeoutMillis = 10_000
    }
    install(ContentNegotiation) {
        json(Json { ignoreUnknownKeys = true })
    }
    install(createClientPlugin("BearerToken") {
        onRequest { request, _ ->
            localStore.token.first()?.let { request.bearerAuth(it) }
        }
    })
    HttpResponseValidator {
        handleResponseExceptionWithRequest { cause, _ ->
            if ((cause as? ClientRequestException)?.response?.status == HttpStatusCode.Unauthorized) {
                localStore.clearToken()
            }
        }
    }
    if (isLoggingEnabled) {
        install(Logging) {
            logger = HttpLogger
            level = LogLevel.BODY
            format = LoggingFormat.OkHttp
            sanitizeHeader { it == HttpHeaders.Authorization }
            filter { !it.url.encodedPath.endsWith("/auth/login") }
        }
    }
    defaultRequest {
        url(serverUrl)
    }
}