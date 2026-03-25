# 아키텍처 & 패키지 구조

## 계층 구조 (4-tier Layered Architecture)

```
Controller → Service → Repository/Mapper → Database
(Presentation)  (Business)  (Persistence)     (Data)
```

## 패키지 구조

```
com.example.api/
├── ApiApplication.java         # Spring Boot 진입점
│
├── common/                          # 공통 패키지
│   ├── advice/ExceptionAdvice.java  # @RestControllerAdvice 전역 예외 처리
│   ├── component/RedisComponent.java # Redis 캐시 유틸리티
│   ├── constants/Constants.java     # 애플리케이션 상수 (BASE_PACKAGE, HIBERNATE_SQL_COMMENT)
│   ├── exception/ApiException.java  # 커스텀 예외 클래스
│   ├── response/
│   │   ├── BaseResponse.java        # 성공 응답 래퍼 (statusCode, message, data)
│   │   └── ErrorResponse.java       # 에러 응답 래퍼 (statusCode, message, method, path, timestamp)
│   └── type/
│       ├── ApiStatus.java           # 상태 코드 enum (200, 800~908)
│       └── RedisKeys.java           # Redis 키 정의 enum
│
├── config/                          # 설정
│   ├── MainDataSourceConfig.java    # DataSource(R/W 분리) + JPA + MyBatis + QueryDSL + ModelMapper 통합 설정
│   └── redis/
│       ├── RedisStandaloneConfig.java  # Redis Standalone (local 프로파일)
│       ├── RedisClusterConfig.java     # Redis Cluster (dev/stg/prd 프로파일)
│       └── RedisTemplateConfig.java    # RedisTemplate 빈 설정
│
├── domain/                          # 도메인 (Entity + JPA Repository)
│   ├── entity/
│   │   ├── Member.java              # 회원 엔티티
│   │   └── Todo.java                # 할일 엔티티
│   └── repository/
│       └── MemberRepository.java    # JpaRepository 인터페이스
│
└── sample/                          # 샘플 패키지 (기능 패키지 템플릿)
    ├── controller/SampleController.java
    ├── service/SampleService.java
    ├── dto/SampleDto.java           # inner static class로 DTO 정의
    ├── repository/SampleRepository.java  # QueryDSL 기반
    └── mapper/SampleMapper.java     # MyBatis 기반
```

## 새 기능 패키지 추가 시

`sample` 패키지를 참고하여 아래 구조로 생성:

```
com.example.api.{패키지명}/
├── controller/{패키지명}Controller.java
├── service/{패키지명}Service.java
├── dto/{패키지명}Dto.java
├── repository/{패키지명}Repository.java   # QueryDSL (필요시)
└── mapper/{패키지명}Mapper.java           # MyBatis (필요시)
```

- Entity는 `domain/entity/`에 추가
- JPA Repository는 `domain/repository/`에 추가
- MyBatis XML은 `resources/mapper/{패키지명}/`에 추가

## 주요 디자인 패턴

| 패턴 | 적용 위치 |
|------|----------|
| Read/Write 분리 | `LazyConnectionDataSourceProxy` - readOnly 트랜잭션 자동 라우팅 |
| DTO 패턴 | API 계약과 도메인 모델 분리 |
| Repository 패턴 | QueryDSL, MyBatis, JPA Query Method 3중 구현 |
| Builder 패턴 | Lombok `@Builder`로 Entity/DTO 생성 |
| 전역 예외 처리 | `@RestControllerAdvice`로 중앙 집중 예외 처리 |
| Enum 기반 상태 코드 | `ApiStatus`로 타입 안전 상태 관리 |
| Cache-Aside | `RedisComponent.getCacheOrDefault()`로 캐시 패턴 구현 |

## 클래스 Javadoc

모든 주요 클래스에 Javadoc 작성 필수. 스타일은 [주석 가이드](commenting.md) 참조.
