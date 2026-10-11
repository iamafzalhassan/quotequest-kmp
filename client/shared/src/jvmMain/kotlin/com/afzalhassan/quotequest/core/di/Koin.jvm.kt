package com.afzalhassan.quotequest.core.di

import com.afzalhassan.quotequest.core.data.DATA_STORE_FILE
import com.afzalhassan.quotequest.core.data.createDataStore
import com.afzalhassan.quotequest.core.data.createHttpClient
import org.koin.core.context.startKoin
import org.koin.dsl.module
import java.io.File

fun initKoin() {
    startKoin {
        modules(appModule, module {
            single { createDataStore(File(System.getProperty("user.home"), ".quotequest/$DATA_STORE_FILE").absolutePath) }
            single { createHttpClient("http://localhost:8080", get()) }
        })
    }
}