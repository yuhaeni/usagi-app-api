# 🐰 우사기(우리의, 사적인,기록) - 일기 감정 기록 API (Emotion Diary API)

하루의 일상을 기록하는 REST API 서비스

## 📌 프로젝트 소개

사용자가 일상의 감정과 어떤 활동을 했는지 기록하는 기능을 제공하는 서비스 입니다.

## 🏗️ 아키텍처
```
┌──────────────┐     ┌──────────────────┐     ┌──────────────┐
│   Client     │────▶│  Spring Boot API │────▶│   Database   │
│  (Mobile)    │◀────│  (Kotlin)        │◀────│ (postgresql) │
└──────────────┘     └──────────────────┘     └──────────────┘
                              │
                              ▼
                     ┌──────────────────┐
                     │   AWS Lightsail  │
                     └──────────────────┘
```

## 🌐 인프라 구성
```
GitHub Actions (CI/CD)
    │
    ▼
Docker Image
    │
    ▼
AWS Lightsail Container Service
(nano, scale: 1)
    │
    └──▶ AWS Lightsail PostgreSQL
         (postgres_15, micro_2.0)
```

## 🛠️ 기술 스택
```
| Language | Kotlin 2.2.21 |
| Framework | Spring Boot 4.0.1 |
| Database | Postgresql 15 |
| ORM | Spring Data JPA |
| Auth | Spring Security + JWT |
| Build | Gradle|
| Infra | AWS Lightsail |
| IaC | Terraform |
| CI/CD | GitHub Actions
| Docs | Swagger (SpringDoc) |
```

## 📋 주요 기능

### ✅ MVP 기능
- [✅] Google 소셜 로그인을 통한 회원가입 / 로그인 (JWT 기반 인증)
- [✅] 일기 CRUD (생성 / 조회 / 수정 / 삭제)
- [✅] 감정 태그 분류 (기쁨, 슬픔, 분노, 불안 등)
- [✅] 액션 카테고리 관리 (데이트, 친구 모임, 가족 행사, 업무 등)


### 🚧 개발 예정
- [ ] 일기 인물 태그 기능 (누구와 하루를 보냈는지)
- [ ] 푸시 알림 연동
- [ ] 액션 카테고리 유저별 커스텀

## 📂 프로젝트 구조
```
├── activityCategory
│    ├── controller
│    │    └── dto
│    ├── entity
│    ├── exception
│    ├── repository
│    └── service
│        └── dto
├── auth
│    ├── controller
│    │    └── dto
│    ├── entity
│    ├── enums
│    ├── exception
│    ├── repository
│    ├── service
│    │    └── dto
│    └── social
│        └── google
├── diary
│    ├── controller
│    │    ├── DiaryController.kt
│    │    └── dto
│    ├── entity
│    ├── enums
│    ├── exception
│    ├── repository
│    └── service
│        └── dto
├── exception
├── global
│    ├── config
│    ├── image
│    ├── redis
│    └── security
│        └── jwt
├── shared
│    ├── dto
│    ├── entity
│    └── tool
├── system
│    ├── controller
│    │    └── dto
│    ├── enums
│    └── service
│        └── dto
└── user
    ├── controller
    │    └── dto
    ├── entity
    ├── enums
    ├── exception
    ├── repository
    └── service
        └── dto
```

## 🔗 API 명세
<img width="1452" height="345" alt="Image" src="https://github.com/user-attachments/assets/da1015ef-1476-4448-83e3-234fb002b2fb" />
<img width="1452" height="294" alt="Image" src="https://github.com/user-attachments/assets/b23c3766-46f6-4968-addd-762e368907f5" />
<img width="1452" height="458" alt="Image" src="https://github.com/user-attachments/assets/cefc4bae-88cd-4d3b-8e16-5482956ceb4f" />

