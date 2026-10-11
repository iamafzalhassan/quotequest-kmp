package com.afzalhassan.quotequest.core.data

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestData
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json

class FakeServer(handler: MockRequestHandler) {
    val requests = mutableListOf<HttpRequestData>()

    val api = QuoteQuestApi(
        HttpClient(
            MockEngine { request ->
                requests += request
                handler(this, request)
            },
        ) {
            expectSuccess = true
            install(ContentNegotiation) { json() }
        },
    )

    fun requestsTo(path: String) = requests.filter { it.url.encodedPath == path }
}

fun MockRequestHandleScope.respondJson(body: String, status: HttpStatusCode = HttpStatusCode.OK) = respond(body, status, headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()))