package com.afzalhassan.quotequest.quote

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/quiz")
class QuizController(
    private val quizService: QuizService,
) {

    @GetMapping
    fun quiz(@RequestParam(defaultValue = "BINARY") mode: QuizMode) = quizService.createQuiz(mode)

    @PostMapping("/answer")
    fun answer(@Valid @RequestBody request: AnswerRequest) = quizService.checkAnswer(request)
}