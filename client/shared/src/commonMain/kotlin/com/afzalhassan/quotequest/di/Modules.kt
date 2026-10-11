package com.afzalhassan.quotequest.di

import com.afzalhassan.quotequest.AppViewModel
import com.afzalhassan.quotequest.data.LocalStore
import com.afzalhassan.quotequest.data.QuoteQuestApi
import com.afzalhassan.quotequest.login.LoginViewModel
import com.afzalhassan.quotequest.profile.ProfileViewModel
import com.afzalhassan.quotequest.quiz.QuizViewModel
import com.afzalhassan.quotequest.settings.SettingsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

internal val appModule = module {
    singleOf(::LocalStore)
    singleOf(::QuoteQuestApi)
    viewModelOf(::AppViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::ProfileViewModel)
    viewModelOf(::SettingsViewModel)
    viewModelOf(::QuizViewModel)
}