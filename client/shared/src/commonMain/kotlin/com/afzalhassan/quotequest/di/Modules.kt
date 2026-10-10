package com.afzalhassan.quotequest.di

import com.afzalhassan.quotequest.data.LocalStore
import com.afzalhassan.quotequest.data.QuoteQuestApi
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

internal val appModule = module {
    singleOf(::LocalStore)
    singleOf(::QuoteQuestApi)
}