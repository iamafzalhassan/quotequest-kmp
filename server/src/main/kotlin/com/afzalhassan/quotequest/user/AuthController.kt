package com.afzalhassan.quotequest.user

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestAttribute
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api")
class AuthController(
    private val userRepository: UserRepository,
    private val authTokenRepository: AuthTokenRepository,
    private val passwordEncoder: PasswordEncoder,
) {

    @PostMapping("/auth/login")
    fun login(@Valid @RequestBody request: LoginRequest): LoginResponse {
        val user = userRepository.findByEmail(request.email)
        if (user == null || !passwordEncoder.matches(request.password, user.passwordHash)) {
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password")
        }
        return LoginResponse(authTokenRepository.save(AuthToken(user)).token)
    }

    @PostMapping("/auth/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun logout(@RequestAttribute(AUTH_TOKEN) authToken: AuthToken) {
        authTokenRepository.delete(authToken)
    }

    @GetMapping("/profile")
    fun profile(@RequestAttribute(AUTH_TOKEN) authToken: AuthToken) = ProfileResponse(authToken.user.email, authToken.user.name)
}