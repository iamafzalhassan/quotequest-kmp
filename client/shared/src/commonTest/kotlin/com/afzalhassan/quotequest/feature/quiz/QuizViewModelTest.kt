package com.afzalhassan.quotequest.feature.quiz

import com.afzalhassan.quotequest.core.data.FakeDataStore
import com.afzalhassan.quotequest.core.data.FakeServer
import com.afzalhassan.quotequest.core.data.LocalStore
import com.afzalhassan.quotequest.core.data.QuestionResponse
import com.afzalhassan.quotequest.core.data.QuizMode
import com.afzalhassan.quotequest.core.data.respondJson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class QuizViewModelTest {

    private val localStore = LocalStore(FakeDataStore())
    private val server = FakeServer { request ->
        when (request.url.encodedPath) {
            "/api/quiz" -> respondJson(questionsJson(count = 2))
            else -> respondJson("""{"correct":true,"rightAnswer":"Author"}""")
        }
    }

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun doubleTapSendsOneAnswer() = runTest {
        val viewModel = QuizViewModel(server.api, localStore)
        viewModel.awaitPlaying()

        viewModel.answer(Answer("Author", answeredYes = true))
        viewModel.answer(Answer("Author", answeredYes = true))
        viewModel.awaitPlaying { it.feedback != null }

        assertEquals(1, server.requestsTo("/api/quiz/answer").size)
    }

    @Test
    fun changingModeRestartsQuiz() = runTest {
        val viewModel = QuizViewModel(server.api, localStore)
        viewModel.awaitPlaying()
        viewModel.answer(Answer("Author", answeredYes = true))
        viewModel.awaitPlaying { it.feedback != null }
        viewModel.next()

        localStore.saveQuizMode(QuizMode.MULTIPLE)

        val playing = viewModel.awaitPlaying { it.mode == QuizMode.MULTIPLE }
        assertEquals(0, playing.index)
        assertEquals(0, playing.score)
        assertEquals("MULTIPLE", server.requestsTo("/api/quiz").last().url.parameters["mode"])
    }

    @Test
    fun savingSameModeDoesNotRestartQuiz() = runTest {
        val viewModel = QuizViewModel(server.api, localStore)
        viewModel.awaitPlaying()

        localStore.saveQuizMode(QuizMode.BINARY)

        assertEquals(1, server.requestsTo("/api/quiz").size)
    }

    @Test
    fun okOnLastQuestionFinishesQuiz() = runTest {
        val viewModel = QuizViewModel(server.api, localStore)

        repeat(2) { question ->
            viewModel.awaitPlaying { it.index == question && it.feedback == null }
            viewModel.answer(Answer("Author", answeredYes = true))
            viewModel.awaitPlaying { it.feedback != null }
            viewModel.next()
        }

        assertEquals(QuizUiState.Finished(score = 2, total = 2), viewModel.state.value)
    }
}

private suspend fun QuizViewModel.awaitPlaying(condition: (QuizUiState.Playing) -> Boolean = { true }) = state.filterIsInstance<QuizUiState.Playing>().first(condition)

private fun questionsJson(count: Int) = Json.encodeToString(
    ListSerializer(QuestionResponse.serializer()),
    List(count) { QuestionResponse(quoteId = it.toLong(), text = "Quote $it", authors = listOf("Author")) },
)