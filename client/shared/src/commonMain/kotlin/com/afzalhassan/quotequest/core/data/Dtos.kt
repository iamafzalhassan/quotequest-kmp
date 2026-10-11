package com.afzalhassan.quotequest.core.data

import kotlinx.serialization.Serializable

enum class QuizMode { BINARY, MULTIPLE }

@Serializable
data class LoginRequest(val email: String, val password: String)

@Serializable
data class LoginResponse(val token: String)

@Serializable
data class ProfileResponse(val email: String, val name: String)

@Serializable
data class QuestionResponse(val quoteId: Long, val text: String, val authors: List<String>)

@Serializable
data class AnswerRequest(val quoteId: Long, val author: String, val answeredYes: Boolean)

@Serializable
data class AnswerResponse(val correct: Boolean, val rightAnswer: String)