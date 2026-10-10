package com.afzalhassan.quotequest.quote

import jakarta.validation.constraints.NotBlank

enum class QuizMode { BINARY, MULTIPLE }

data class QuestionResponse(
    val quoteId: Long,
    val text: String,
    val authors: List<String>,
)

data class AnswerRequest(
    val quoteId: Long,
    @field:NotBlank val author: String,
    val answeredYes: Boolean = true,
)

data class AnswerResponse(
    val correct: Boolean,
    val rightAnswer: String,
)