package com.afzalhassan.quotequest.quote

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import kotlin.random.Random

@Service
class QuizService(
    private val quoteRepository: QuoteRepository,
) {

    fun createQuiz(mode: QuizMode): List<QuestionResponse> {
        val quotes = quoteRepository.findAll()
        val allAuthors = quotes.map { it.author }.distinct()

        return quotes.shuffled().take(QUESTIONS_PER_SESSION).map { quote ->
            val wrongAuthors = (allAuthors - quote.author).shuffled()
            val authors = when (mode) {
                QuizMode.BINARY -> listOf(if (Random.nextBoolean()) quote.author else wrongAuthors.first())
                QuizMode.MULTIPLE -> (wrongAuthors.take(2) + quote.author).shuffled()
            }
            QuestionResponse(quote.id!!, quote.text, authors)
        }
    }

    fun checkAnswer(request: AnswerRequest): AnswerResponse {
        val quote = quoteRepository.findByIdOrNull(request.quoteId) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Quote not found")
        val correct = (request.author == quote.author) == request.answeredYes
        return AnswerResponse(correct, quote.author)
    }

    companion object {
        const val QUESTIONS_PER_SESSION = 10
    }
}