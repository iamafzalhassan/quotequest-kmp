package com.afzalhassan.quotequest.core.di

import android.content.Context
import com.afzalhassan.quotequest.core.data.DATA_STORE_FILE
import com.afzalhassan.quotequest.core.data.createDataStore
import com.afzalhassan.quotequest.core.data.createHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

fun initKoin(context: Context) {
    startKoin {
        androidContext(context)
        modules(appModule, module {
            single { createDataStore(androidContext().filesDir.resolve(DATA_STORE_FILE).absolutePath) }
            single { createHttpClient("http://10.0.2.2:8080", get()) }
        })
    }
}