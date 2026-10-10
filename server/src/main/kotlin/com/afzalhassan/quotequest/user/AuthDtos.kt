package com.afzalhassan.quotequest.user

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank val email: String,
    @field:NotBlank val password: String,
)

data class LoginResponse(val token: String)

data class ProfileResponse(
    val email: String,
    val name: String,
)