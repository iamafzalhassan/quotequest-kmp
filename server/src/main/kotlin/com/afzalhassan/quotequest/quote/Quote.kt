package com.afzalhassan.quotequest.quote

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class Quote(
    @Column(nullable = false, length = 500)
    val text: String,

    @Column(nullable = false)
    val author: String,

    @Id
    @GeneratedValue
    var id: Long? = null,
)

interface QuoteRepository : JpaRepository<Quote, Long>