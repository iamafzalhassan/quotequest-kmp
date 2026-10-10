package com.afzalhassan.quotequest

import android.app.Application
import com.afzalhassan.quotequest.di.initKoin

class QuoteQuestApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this)
    }
}