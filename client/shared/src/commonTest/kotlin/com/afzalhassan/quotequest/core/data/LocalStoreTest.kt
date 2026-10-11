package com.afzalhassan.quotequest.core.data

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertFalse

class LocalStoreTest {

    private val localStore = LocalStore(FakeDataStore())

    @Test
    fun withoutATokenNothingExpires() = runTest {
        localStore.expireSession()

        assertFalse(localStore.isSessionExpired.first())
    }
}