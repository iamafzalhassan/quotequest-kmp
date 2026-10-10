package com.afzalhassan.quotequest.user

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

@Entity
class AuthToken(
    @ManyToOne(optional = false)
    val user: User,

    @Id
    val token: String = UUID.randomUUID().toString(),
)

interface AuthTokenRepository : JpaRepository<AuthToken, String>