# QuoteQuest – Famous Quote Quiz

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Compose Multiplatform](https://img.shields.io/badge/Compose_Multiplatform-1.12.1-4285F4?logo=jetpackcompose&logoColor=white)
![Platforms](https://img.shields.io/badge/platforms-Android_%7C_Desktop-3DDC84?logo=android&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.1.1-6DB33F?logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?logo=openjdk&logoColor=white)

A tabbed quiz app where the user guesses who said a famous quote.

- **Client:** Kotlin Multiplatform + Compose Multiplatform (Android, Desktop)
- **Server:** Spring Boot (Kotlin) REST API with an embedded H2 database

## Screenshots

| Login | Quiz | Settings | Profile |
|:---:|:---:|:---:|:---:|
| <img src="https://github.com/user-attachments/assets/8a463e15-db81-44e8-81f3-44e1bc6d7768" width="200" alt="Login screen"> | <img src="https://github.com/user-attachments/assets/0c8d9f33-4755-4f54-b905-e1cef5c525f4" width="200" alt="Quiz screen"> | <img src="https://github.com/user-attachments/assets/cb5522c4-d962-49dc-9646-d4f6b99d476f" width="200" alt="Settings screen"> | <img src="https://github.com/user-attachments/assets/1fa0a450-5352-4a6d-b035-61c20c4e5136" width="200" alt="Profile screen"> |

```
quotequest/
├── server/   Spring Boot server
└── client/   KMP + Compose Multiplatform app
```
---

## Server

### Requirements
- JDK 21

### Run
```bash
cd server
./gradlew bootRun        # Windows: gradlew.bat bootRun
```
The API runs at `http://localhost:8080` (from the Android emulator: `http://10.0.2.2:8080`).

The database is in-memory and is re-seeded on every start with **20 famous quotes** and **2 demo users**.

### Demo users
| Email | Password |
|---|---|
| `afzal@mail.com` | `135@Afzal` |
| `dimitar@mail.com` | `579@Dimitar` |

### Run tests
```bash
cd server
./gradlew test
```

### H2 console (development only)
`http://localhost:8080/h2-console` with JDBC URL `jdbc:h2:mem:quotequest`, user `sa` and an empty password.

---

## API

Base URL: `http://localhost:8080/api`. All endpoints except **login** require the header:
```
Authorization: Bearer <token>
```

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/auth/login` | Log in and receive a token |
| `POST` | `/auth/logout` | Invalidate the current token |
| `GET` | `/profile` | Get the logged-in user's profile |
| `GET` | `/quiz?mode=BINARY\|MULTIPLE` | Get a new session of 10 questions (default: `BINARY`) |
| `POST` | `/quiz/answer` | Check an answer |

### `POST /auth/login`
```json
// Request
{ "email": "afzal@mail.com", "password": "135@Afzal" }

// 200 OK
{ "token": "3f2b8c1e-6a4d-4e2b-9f7a-1c5d8e9b0a12" }
```
`401` if the email or password is wrong. `400` if a field is empty.

### `POST /auth/logout`
`204 No Content`. The token is deleted and stops working immediately.

### `GET /profile`
```json
// 200 OK
{ "email": "afzal@mail.com", "name": "Afzal" }
```

### `GET /quiz?mode=MULTIPLE`
Returns 10 random, unique questions. **The correct answer is never included.**
```json
// 200 OK
[
  { "quoteId": 4, "text": "Imagination is more important than knowledge.",
    "authors": ["Isaac Newton", "Albert Einstein", "Oscar Wilde"] }
]
```
- `MULTIPLE`: 3 authors (the real one plus 2 distinct distractors), shuffled.
- `BINARY`: 1 author ("Did this person say it?"), which is the real author about half of the time.

### `POST /quiz/answer`
```json
// Request (multiple choice: the author the user picked)
{ "quoteId": 4, "author": "Albert Einstein" }

// Request (binary: the author shown + the user's Yes/No)
{ "quoteId": 4, "author": "Isaac Newton", "answeredYes": false }

// 200 OK
{ "correct": true, "rightAnswer": "Albert Einstein" }
```
`answeredYes` defaults to `true`. `404` if the quote does not exist.

### Errors
All errors use the standard [RFC 9457 Problem Details](https://www.rfc-editor.org/rfc/rfc9457) format:
```json
{ "title": "Unauthorized", "status": 401, "detail": "Invalid email or password", "instance": "/api/auth/login" }
```

---

## Server design decisions

- **The server checks answers.** Questions never contain the correct author, so it cannot be read from network traffic. One rule covers both modes: `correct = (author == realAuthor) == answeredYes`.
- **The server generates the answer options,**, as the task requires.
- **Opaque token stored in the database, not a JWT.** Logout deletes the token, so the session ends immediately. A JWT stays valid until it expires unless a blocklist is added.
- **A small interceptor instead of full Spring Security.** The app needs a single token check. `spring-security-crypto` is used only for BCrypt password hashing.
- **Passwords are hashed with BCrypt.**
- **Seeding with an `ApplicationRunner`** so the demo passwords can be hashed at startup. The `count() == 0` guard makes it safe to run repeatedly.
- **Organised by feature** (`quote/`, `user/`) rather than by layer.

---

## Client

### Requirements
- JDK 21
- Android Studio with an emulator (API 26+) for the Android app
- The server running (see above)

### Run
**Android** (emulator):
```bash
cd client
./gradlew :androidApp:installDebug      # Windows: gradlew.bat :androidApp:installDebug
```
The emulator reaches the server at `http://10.0.2.2:8080`. This is set as `SERVER_URL` in `androidApp/build.gradle.kts`.

**Desktop:**
```bash
cd client
./gradlew :desktopApp:run
```
The app connects to `http://localhost:8080`. A different server can be set with the JVM option `-Dquotequest.serverUrl=<url>`. HTTP logs are printed to the console when the app is started through Gradle.

### Run tests
```bash
cd client
./gradlew :shared:allTests
```

### Tech stack
| | |
|---|---|
| Language / UI | Kotlin 2.4.20, Compose Multiplatform 1.12.1 (Material 3) |
| Networking | Ktor 3.6.0 (OkHttp engine), kotlinx.serialization |
| Dependency injection | Koin 4.2.2 |
| Storage | DataStore Preferences 1.2.1 |
| Navigation | JetBrains Navigation Compose 2.10.0-rc01 (type-safe routes) |
| Android | minSdk 26, targetSdk 37 |
| Testing | kotlin-test, kotlinx-coroutines-test, Ktor MockEngine |

### Structure
```
client/
├── androidApp/       Android entry point: Application (starts Koin), MainActivity (splash, edge-to-edge)
├── desktopApp/       Desktop entry point: window setup
└── shared/src/
    ├── commonMain/   all shared code (com.afzalhassan.quotequest)
    │   ├── App.kt, AppViewModel.kt, Route.kt    app shell and navigation
    │   ├── core/data/   API client, HTTP setup, DataStore storage, DTOs
    │   ├── core/di/     Koin module
    │   ├── core/ui/     theme, shared components, error messages
    │   └── feature/     login, main (tabs), quiz, settings, profile, splash
    ├── androidMain/  Android setup: DataStore path, server URL, splash timing
    ├── jvmMain/      Desktop setup: DataStore path, server URL, splash timing, window icon
    └── commonTest/   ViewModel and storage tests
```

### Architecture
Each screen has a ViewModel that exposes **one `UiState`** as a `StateFlow`: state flows down, events flow up. A screen is a thin composable that collects the state with `collectAsStateWithLifecycle`, plus a stateless `*Content` composable that only draws it.

ViewModels use `QuoteQuestApi` (Ktor) and `LocalStore` (DataStore), injected by Koin.

**Navigation follows the session.** The token in DataStore is the single source of truth. `App` observes it and shows Login or Main. Logging in, logging out and session expiry only change the token; no screen navigates on its own.

---

## Client design decisions

- **Shared UI with Compose Multiplatform.** All screens, ViewModels and data code are in `commonMain`. Platform code is limited to startup, the DataStore file path, the server URL and splash timing.
- **Koin instead of Hilt.** Hilt is Android-only, while Koin works in common code.
- **DataStore instead of SharedPreferences.** It is asynchronous, Flow-based, works on both platforms and never reads the disk on the main thread.
- **Session-driven navigation.** Screens can't disagree about whether the user is logged in. A `401` on any request clears the token, and the app returns to login.
- **The quiz is a sealed state machine** (`Loading`, `Error`, `Playing`, `Finished`), so impossible combinations can't be represented.
- **Coroutine race conditions handled in the quiz:**
  - a pending answer blocks a second tap;
  - changing the mode cancels any in-flight answer and reloads with `collectLatest`;
  - `CancellationException` is always rethrown.
- **Error messages are string resources in the state**, so ViewModels stay testable and texts can be translated. Wrong credentials get their own message.
- **Logout always works locally**, even if the server can't be reached.
- **Splash screen:**
  - Android uses the system splash API and keeps it on screen only until the session is read, with no artificial delay.
  - Desktop shows its own splash for at least 500 ms so it doesn't flash.
- **Server URL is set by each app** (`BuildConfig` on Android, a system property on Desktop), not inside shared code.
- **Security basics:**
  - the token is sent as a Bearer header and hidden from logs;
  - login requests are never logged;
  - HTTP logging is on only in debug builds;
  - app data is excluded from backups;
  - plain HTTP is allowed only for `10.0.2.2` and `localhost`.
- **Packages, not Gradle modules,** for `core` and `feature`. That is enough at this size; modules would make sense as the app grows.
- **Own design system** (colours, spacing, text styles, components) on top of Material 3, with the Inter font.

## Client testing

Tests are **risk-based**: they cover places where bugs are likely or costly, not every line. Real ViewModels and `LocalStore` run against fakes at the edges: Ktor's `MockEngine` as the server and an in-memory DataStore.

| Test | Risk it covers |
|---|---|
| Wrong credentials show the invalid-credentials message | A regression found during review (a `401` showed a generic error) |
| Successful login saves the token and trims the email | Login is the entry point; stray spaces break login |
| Double tap sends one answer | Race condition: score counted twice |
| Changing the mode restarts the quiz | The mode is read from storage as a Flow |
| Saving the same mode does not restart the quiz | The user would lose their progress for no reason |
| OK on the last question finishes the quiz | End of the state machine |
| Without a token nothing expires | A late `401` must not show "session expired" after logout |

## Client known limitations

- **The token is stored unencrypted.**
- **No repository layer.** ViewModels call the API and storage directly, handle Ktor exceptions and use DTOs as UI models. I’d add one when there’s caching or a second data source.
- **Quiz progress is lost on process death.** It survives rotation because the ViewModel survives configuration changes.
- **Release builds are not minified** (R8 is off).
- **HTTP only**, aimed at a local server.
- **Pre-release libraries:** Navigation `2.10.0-rc01` (matches Lifecycle 2.11) and Material 3 `1.12.0-alpha03`.

## Credits
- [Inter](https://rsms.me/inter/) font by Rasmus Andersson, SIL Open Font License 1.1 (`client/licenses/Inter-OFL.txt`).

## Development notes
I used Claude Code (an AI coding assistant) throughout this task to explain concepts, suggest approaches, write code snippets and review my changes. I adjusted it where needed and reviewed every change. I can explain the decisions in it.
