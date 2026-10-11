package com.afzalhassan.quotequest.quiz

import com.afzalhassan.quotequest.data.AnswerResponse
import com.afzalhassan.quotequest.data.QuestionResponse
import com.afzalhassan.quotequest.data.QuizMode
import org.jetbrains.compose.resources.StringResource

sealed interface QuizUiState {

    data object Loading : QuizUiState

    data class Error(val message: StringResource) : QuizUiState

    data class Playing(
        val mode: QuizMode,
        val questions: List<QuestionResponse>,
        val index: Int = 0,
        val score: Int = 0,
        val pendingAnswer: Answer? = null,
        val feedback: AnswerResponse? = null,
        val error: StringResource? = null,
    ) : QuizUiState {
        val question: QuestionResponse get() = questions[index]
    }

    data class Finished(val score: Int, val total: Int) : QuizUiState
}

data class Answer(val author: String, val answeredYes: Boolean)