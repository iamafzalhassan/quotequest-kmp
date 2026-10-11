package com.afzalhassan.quotequest.core.di

import com.afzalhassan.quotequest.AppViewModel
import com.afzalhassan.quotequest.core.data.LocalStore
import com.afzalhassan.quotequest.core.data.QuoteQuestApi
import com.afzalhassan.quotequest.feature.login.LoginViewModel
import com.afzalhassan.quotequest.feature.profile.ProfileViewModel
import com.afzalhassan.quotequest.feature.quiz.QuizViewModel
import com.afzalhassan.quotequest.feature.settings.SettingsViewModel
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