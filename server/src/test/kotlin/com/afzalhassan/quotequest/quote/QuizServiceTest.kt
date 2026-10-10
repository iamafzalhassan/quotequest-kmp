package com.afzalhassan.quotequest.quote

import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.mock
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.util.Optional
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class QuizServiceTest {

    private val quotes = (1..12L).map { Quote("Quote $it", "Author ${it % 4}", id = it) }
    private val repository = mock(QuoteRepository::class.java)
    private val service = QuizService(repository)

    init {
        given(repository.findAll()).willReturn(quotes)
    }

    @Test
    fun `multiple choice gives 10 different questions with 3 distinct authors including the real one`() {
        val questions = service.createQuiz(QuizMode.MULTIPLE)

        assertEquals(QuizService.QUESTIONS_PER_SESSION, questions.size)
        assertEquals(questions.size, questions.map { it.quoteId }.distinct().size)
        questions.forEach { question ->
            val realAuthor = quotes.first { it.id == question.quoteId }.author
            assertEquals(3, question.authors.distinct().size)
            assertTrue(realAuthor in question.authors)
        }
    }

    @Test
    fun `binary gives one author per question`() {
        val questions = service.createQuiz(QuizMode.BINARY)

        assertEquals(QuizService.QUESTIONS_PER_SESSION, questions.size)
        assertTrue(questions.all { it.authors.size == 1 })
    }

    @Test
    fun `answer is correct only when yes or no matches the real author`() {
        given(repository.findById(1L)).willReturn(Optional.of(quotes[0]))
        val realAuthor = quotes[0].author

        assertTrue(service.checkAnswer(AnswerRequest(1L, realAuthor, answeredYes = true)).correct)
        assertFalse(service.checkAnswer(AnswerRequest(1L, realAuthor, answeredYes = false)).correct)
        assertTrue(service.checkAnswer(AnswerRequest(1L, "Someone else", answeredYes = false)).correct)
        assertFalse(service.checkAnswer(AnswerRequest(1L, "Someone else", answeredYes = true)).correct)
    }

    @Test
    fun `unknown quote returns 404`() {
        given(repository.findById(99L)).willReturn(Optional.empty())

        val error = assertFailsWith<ResponseStatusException> {
            service.checkAnswer(AnswerRequest(99L, "X"))
        }
        assertEquals(HttpStatus.NOT_FOUND, error.statusCode)
    }
}