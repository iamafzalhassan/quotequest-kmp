package com.afzalhassan.quotequest.quiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.afzalhassan.quotequest.data.AnswerRequest
import com.afzalhassan.quotequest.data.LocalStore
import com.afzalhassan.quotequest.data.QuizMode
import com.afzalhassan.quotequest.data.QuoteQuestApi
import com.afzalhassan.quotequest.toMessage
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.quiz_no_questions

class QuizViewModel(
    private val api: QuoteQuestApi,
    localStore: LocalStore,
) : ViewModel() {

    private val _state = MutableStateFlow<QuizUiState>(QuizUiState.Loading)
    val state: StateFlow<QuizUiState> = _state.asStateFlow()

    private val restarts = MutableStateFlow(0)
    private var answerJob: Job? = null

    init {
        viewModelScope.launch {
            combine(localStore.quizMode, restarts) { mode, _ -> mode }.collectLatest { mode -> load(mode) }
        }
    }

    fun restart() {
        restarts.update { it + 1 }
    }

    fun answer(answer: Answer) {
        val playing = _state.value as? QuizUiState.Playing ?: return
        if (playing.pendingAnswer != null || playing.feedback != null) return

        val quoteId = playing.question.quoteId
        updatePlaying { it.copy(pendingAnswer = answer, error = null) }
        answerJob = viewModelScope.launch {
            try {
                val result = api.answer(AnswerRequest(quoteId, answer.author, answer.answeredYes))
                updatePlaying {
                    it.copy(
                        pendingAnswer = null,
                        feedback = result,
                        score = if (result.correct) it.score + 1 else it.score,
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                updatePlaying { it.copy(pendingAnswer = null, error = e.toMessage()) }
            }
        }
    }

    fun next() {
        updatePlaying { playing ->
            when {
                playing.feedback == null -> playing
                playing.index + 1 < playing.questions.size -> playing.copy(
                    index = playing.index + 1,
                    feedback = null
                )

                else -> QuizUiState.Finished(playing.score, playing.questions.size)
            }
        }
    }

    private suspend fun load(mode: QuizMode) {
        answerJob?.cancel()
        _state.value = QuizUiState.Loading
        try {
            val questions = api.quiz(mode)
            _state.value = if (questions.isEmpty()) {
                QuizUiState.Error(Res.string.quiz_no_questions)
            } else {
                QuizUiState.Playing(mode, questions)
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            _state.value = QuizUiState.Error(e.toMessage())
        }
    }

    private fun updatePlaying(transform: (QuizUiState.Playing) -> QuizUiState) {
        _state.update { if (it is QuizUiState.Playing) transform(it) else it }
    }
}