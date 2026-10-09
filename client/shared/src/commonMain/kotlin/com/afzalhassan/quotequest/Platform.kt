package com.afzalhassan.quotequest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform