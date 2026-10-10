# QuoteQuest – Famous Quote Quiz

A tabbed quiz app where the user guesses who said a famous quote.

- **Client:** Kotlin Multiplatform + Compose Multiplatform (Android, Desktop)
- **Server:** Spring Boot (Kotlin) REST API with an embedded H2 database

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
- **The server generates the answer options** (one correct author plus distinct distractors), as the task requires.
- **Opaque token stored in the database, not a JWT.** Logout deletes the token, so the session ends immediately. A JWT stays valid until it expires unless a blocklist is added.
- **A small interceptor instead of full Spring Security.** The app needs a single token check. `spring-security-crypto` is used only for BCrypt password hashing.
- **Passwords are hashed with BCrypt** (salted, deliberately slow). Login returns the same error for an unknown email and a wrong password, so it doesn't reveal which emails exist.
- **DTOs, not entities, in responses.** The `User` entity (with its password hash) is never serialized.
- **Seeding with an `ApplicationRunner`** so the demo passwords can be hashed at startup. The `count() == 0` guard makes it safe to run repeatedly.
- **`open-in-view` disabled**, so database access stays inside repositories and services.
- **Organised by feature** (`quote/`, `user/`) rather than by layer.

---

## Client
