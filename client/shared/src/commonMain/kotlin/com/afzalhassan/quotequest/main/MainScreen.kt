package com.afzalhassan.quotequest.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.afzalhassan.quotequest.data.QuoteQuestApi
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun MainScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Main") }
}