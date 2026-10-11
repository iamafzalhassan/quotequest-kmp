package com.afzalhassan.quotequest.di

import com.afzalhassan.quotequest.AppViewModel
import com.afzalhassan.quotequest.data.LocalStore
import com.afzalhassan.quotequest.data.QuoteQuestApi
import com.afzalhassan.quotequest.login.LoginViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    singleOf(::LocalStore)
    singleOf(::QuoteQuestApi)
    viewModelOf(::AppViewModel)
    viewModelOf(::LoginViewModel)
}