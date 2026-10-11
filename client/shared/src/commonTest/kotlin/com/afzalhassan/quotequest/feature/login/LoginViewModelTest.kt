package com.afzalhassan.quotequest.feature.login

import com.afzalhassan.quotequest.core.data.FakeDataStore
import com.afzalhassan.quotequest.core.data.FakeServer
import com.afzalhassan.quotequest.core.data.LocalStore
import com.afzalhassan.quotequest.core.data.respondJson
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.TextContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import quotequest.shared.generated.resources.Res
import quotequest.shared.generated.resources.error_invalid_credentials
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest {

    private val localStore = LocalStore(FakeDataStore())

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun wrongCredentialsShowInvalidCredentialsMessage() = runTest {
        val viewModel = LoginViewModel(FakeServer { respondJson("", HttpStatusCode.Unauthorized) }.api, localStore)
        viewModel.onEmailChange("afzal@mail.com")
        viewModel.onPasswordChange("wrong")

        viewModel.login()

        assertEquals(Res.string.error_invalid_credentials, viewModel.state.first { !it.isLoading }.formError)
    }

    @Test
    fun successfulLoginSavesTokenAndTrimsEmail() = runTest {
        val server = FakeServer { respondJson("""{"token":"abc"}""") }
        val viewModel = LoginViewModel(server.api, localStore)
        viewModel.onEmailChange("  afzal@mail.com ")
        viewModel.onPasswordChange("secret")

        viewModel.login()

        assertEquals("abc", localStore.token.first { it != null })
        assertContains((server.requests.single().body as TextContent).text, "\"email\":\"afzal@mail.com\"")
    }
}