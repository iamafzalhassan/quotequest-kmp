package com.afzalhassan.quotequest.user

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

const val AUTH_TOKEN = "authToken"

@Component
class AuthInterceptor(
    private val authTokenRepository: AuthTokenRepository,
) : HandlerInterceptor {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val token = request.getHeader("Authorization")?.removePrefix("Bearer ")
        val authToken = token?.let { authTokenRepository.findByIdOrNull(it) } ?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not logged in")
        request.setAttribute(AUTH_TOKEN, authToken)
        return true
    }
}

@Configuration
class WebConfig(
    private val authInterceptor: AuthInterceptor,
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(authInterceptor)
            .addPathPatterns("/api/**")
            .excludePathPatterns("/api/auth/login")
    }
}