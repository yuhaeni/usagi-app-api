# 🐰 우사기(우리의, 사적인,기록) - 일기 감정 기록 API (Emotion Diary API)

하루의 감정을 기록하고 분석하는 REST API 서비스

## 📌 프로젝트 소개

사용자가 일상의 감정을 기록하고, 감정 패턴을 분석할 수 있는 백엔드 서비스입니다.

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

| 분류 | 기술 |
|------|------|

| **Language** | Kotlin 2.2.21 |
| **Framework** | Spring Boot 4.0.1 |
| **Database** | Postgresql 15 |
| **ORM** | Spring Data JPA |
| **Auth** | Spring Security + JWT |
| **Build** | Gradle|
| **Infra** | AWS Lightsail |
| **IaC** | Terraform |
| **CI/CD** | GitHub Actions
| **Docs** | Swagger (SpringDoc) |

## 📋 주요 기능

### ✅ MVP 기능
- [✅] Google 소셜 로그인을 통한 회원가입 / 로그인 (JWT 기반 인증)
- [✅] 일기 CRUD (생성 / 조회 / 수정 / 삭제)
- [✅] 감정 태그 분류 (기쁨, 슬픔, 분노, 불안 등)
- [✅] 액션 카테고리 관리 (데이트, 친구 모임, 가족 행사, 업무 등)


### 🚧 개발 예정
- [ ] 일기 인물 태그 기능 (누구와 하루를 보냈는지)
- [ ] DDD 설계 적용
- [ ] 푸시 알림 연동
- [ ] 액션 카테고리 유저별 커스텀

## 📂 프로젝트 구조
*** DDD 설계 적용 후 수정 추가


## 🔗 API 명세
<img width="1423" height="231" alt="image" src="https://github.com/user-attachments/assets/3f04a171-d33e-4057-9f3c-c0c445c37dbe" />
<img width="1416" height="344" alt="image" src="https://github.com/user-attachments/assets/d0e717bc-0f38-4e75-9bac-cf1c60a27c72" />
<img width="1416" height="287" alt="image" src="https://github.com/user-attachments/assets/8d6020dc-36ed-46ff-a005-dc06b1f5b734" />
<img width="1416" height="109" alt="image" src="https://github.com/user-attachments/assets/16f81f52-22cd-48b2-b0e5-154ac743f70f" />

