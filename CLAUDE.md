# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**usagi-app-api** is a REST API backend for an emotion/diary journaling app ("우사기 - 우리의, 사적인, 기록"). Built with Kotlin + Spring Boot, following Domain-Driven Design.

## Commands

```bash
# Build
./gradlew build
./gradlew clean build

# Run locally
./gradlew bootRun

# Test (full suite)
./gradlew test

# Test (excluding local-only, as used in CI)
./gradlew test -PexcludeTags="local-only"

# Test (single class or method)
./gradlew test --tests "com.kou.usagiappapi.diary.service.DiaryServiceTest"
./gradlew test --tests "com.kou.usagiappapi.diary.service.DiaryServiceTest.methodName"

# Lint
./gradlew ktlintCheck
./gradlew ktlintFormat
```

## Architecture

### Domain Structure

Each domain lives under `src/main/kotlin/com/kou/usagiappapi/` and is self-contained:

- **auth** — Google OAuth2 login, JWT access/refresh token lifecycle
- **user** — User profiles, ACTIVE/WITHDRAWN status
- **diary** — Core diary CRUD, emotion tags, activity associations
- **activityCategory** — Activity/action tag categories
- **system** — System constants/configuration endpoints
- **global** — Cross-cutting infrastructure: Spring Security config, JWT filter, Cloudinary image management, Redis client
- **shared** — Base entities, `ApiResponse<T>` wrapper, common DTOs
- **exception** — Global exception handler; each domain has its own exception hierarchy

### Key Patterns

**Layered within each domain:** `controller/dto → service/dto → repository → entity`  
Request DTOs and response DTOs are separate; services never return entities directly.

**Standard API response wrapper:**
```kotlin
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    val errorCode: String? = null,
)
```

**Entity classes:** `@Entity`, `@MappedSuperclass`, `@Embeddable` 클래스는 `kotlin("plugin.allopen")`으로 자동 `open` 처리됨 (`build.gradle.kts`). Hibernate가 LAZY 프록시 서브클래스를 만들기 위해 필수 — 빠지면 `@ManyToOne(fetch = LAZY)`가 조용히 EAGER로 fallback됨.

IntelliJ에서 테스트 실행 시 컴파일러 플러그인이 적용되도록 Settings → Build Tools → Gradle에서 **Build and run using / Run tests using**을 모두 `Gradle`로 설정해야 함.

**Testing stack:**
- Kotest `DescribeSpec` DSL for all tests
- MockK for mocking
- TestContainers spins up real PostgreSQL (postgres:15-alpine) and Redis (redis:alpine) for integration tests
- Integration tests extend `IntegrationTestSupport` and run under `spring.profiles.active=test`

### Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin 2.x, JDK 21 |
| Framework | Spring Boot 4.0.1 |
| ORM | Spring Data JPA (Hibernate) |
| Database | PostgreSQL 15 |
| Auth | Spring Security, JWT (jjwt 0.12.6), Google OAuth2 |
| Cache/Session | Spring Data Redis |
| Images | Cloudinary (auto-resized to 500×300 for diaries) |
| API Docs | SpringDoc OpenAPI 2.x (Swagger UI) |
| Build | Gradle 9 + KtLint 14 |

### Configuration Profiles

- `local` — local development
- `test` — integration tests with TestContainers
- `dev` — deployed dev environment (AWS Lightsail)

### Deployment

CI/CD via GitHub Actions: PRs to `dev` run tests; pushes to `dev` build a Docker image and deploy to AWS Lightsail (Spring Boot app + Nginx + Redis containers).
