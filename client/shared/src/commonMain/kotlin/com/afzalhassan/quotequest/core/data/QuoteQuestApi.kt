package com.afzalhassan.quotequest.core.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class QuoteQuestApi(private val client: HttpClient) {

    suspend fun login(request: LoginRequest): LoginResponse = client.post("/api/auth/login") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()

    suspend fun logout() {
        client.post("/api/auth/logout")
    }

    suspend fun profile(): ProfileResponse = client.get("/api/profile").body()

    suspend fun quiz(mode: QuizMode): List<QuestionResponse> = client.get("/api/quiz") {
        parameter("mode", mode.name)
    }.body()

    suspend fun answer(request: AnswerRequest): AnswerResponse = client.post("/api/quiz/answer") {
        contentType(ContentType.Application.Json)
        setBody(request)
    }.body()
}