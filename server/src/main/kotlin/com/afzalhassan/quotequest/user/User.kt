package com.afzalhassan.quotequest.user

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.springframework.data.jpa.repository.JpaRepository

@Entity
@Table(name = "users")
class User(
    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val name: String,

    @Id
    @GeneratedValue
    var id: Long? = null,
)

interface UserRepository : JpaRepository<User, Long> {
    fun findByEmail(email: String): User?
}