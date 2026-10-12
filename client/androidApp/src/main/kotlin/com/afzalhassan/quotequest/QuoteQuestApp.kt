package com.afzalhassan.quotequest

import android.app.Application
import com.afzalhassan.quotequest.core.di.initKoin

class QuoteQuestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this, BuildConfig.SERVER_URL)
    }
}