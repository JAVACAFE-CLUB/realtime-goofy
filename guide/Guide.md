# Realtime-Goofy 프로젝트를 위한 AI LLM 가이드

## 목차
1. [소개](#introduction)
2. [프로젝트 아키텍처](#project-architecture)
3. [핵심 구성 요소](#core-components)
4. [API 사용법](#api-usage)
5. [애플리케이션 확장](#extending-the-application)
6. [모범 사례](#best-practices)

## 소개

이 가이드는 Kotlin과 Spring Boot를 사용하여 구축된 Realtime-Goofy 프로젝트에 대한 포괄적인 개요를 제공합니다. 이 프로젝트는 헥사고날 아키텍처(포트 및 어댑터라고도 함)를 따라 관심사를 명확히 분리하고 코드베이스를 더 유지 관리하기 쉽고 테스트하기 쉽게 만듭니다.

## 프로젝트 아키텍처

이 프로젝트는 다음 계층으로 구성된 헥사고날 아키텍처를 따릅니다:

### 도메인 계층
비즈니스 로직과 도메인 모델을 포함하는 애플리케이션의 핵심입니다. 이 계층은 데이터베이스나 웹 프레임워크와 같은 외부 관심사와 독립적입니다.

### 인바운드 어댑터
외부 시스템이 애플리케이션과 상호 작용할 수 있게 하는 구성 요소입니다. 이 프로젝트에서는 주로 데이터베이스 리포지토리입니다.

### 아웃바운드 어댑터
애플리케이션이 외부 시스템과 상호 작용할 수 있게 하는 구성 요소입니다. 이 프로젝트에서는 주로 REST 컨트롤러입니다.

### 공통 유틸리티
애플리케이션 전체에서 사용되는 공유 유틸리티, 모델 및 확장 기능입니다.

## 핵심 구성 요소

### 도메인 계층
- **값 객체(VO)**: `TrendId`와 같은 도메인 개념을 나타내는 불변 객체입니다.
- **서비스**: `TrendService`와 같은 비즈니스 로직을 구현하는 클래스입니다.

### 인바운드 어댑터
- **엔티티**: `Trend`와 같은 데이터베이스 테이블에 매핑되는 JPA 엔티티입니다.
- **리포지토리**: `TrendJpaRepository`와 같은 데이터 액세스를 제공하는 Spring Data JPA 리포지토리입니다.

### 아웃바운드 어댑터
- **DTO**: `TrendCreateRequest`, `TrendUpdateRequest`, `TrendResponse`와 같은 API 요청 및 응답에 사용되는 데이터 전송 객체입니다.
- **리소스**: `TrendResource` 및 `HealthResource`와 같은 API 엔드포인트를 노출하는 REST 컨트롤러입니다.

### 공통 유틸리티
- **DTO**: 애플리케이션 전체에서 사용되는 공통 데이터 전송 객체입니다.
- **확장 기능**: 추가 기능을 제공하는 Kotlin 확장 함수입니다.
- **모델**: 애플리케이션 전체에서 사용되는 공통 모델 클래스입니다.

## API 사용법

애플리케이션은 트렌드 관리를 위한 REST API를 제공합니다. 사용 방법은 다음과 같습니다:

### 헬스 체크
```
GET /api/v1/health
```
현재 환경 및 상태 메시지를 반환합니다.

### 트렌드 생성
```
POST /api/v1/trends
Content-Type: application/json

{
  "title": "예시 트렌드",
  "content": "이것은 예시 트렌드입니다",
  "seq": 1
}
```
제공된 제목, 내용 및 시퀀스 번호로 새 트렌드를 생성합니다.

### 모든 트렌드 조회
```
GET /api/v1/trends
```
모든 트렌드의 페이지네이션된 목록을 반환합니다.

### 특정 트렌드 조회
```
GET /api/v1/trends/{id}
```
지정된 ID의 트렌드를 반환합니다.

### 트렌드 업데이트
```
PUT /api/v1/trends/{id}
Content-Type: application/json

{
  "title": "업데이트된 트렌드",
  "content": "이것은 업데이트된 트렌드입니다",
  "seq": 2
}
```
지정된 ID의 트렌드를 제공된 제목, 내용 및 시퀀스 번호로 업데이트합니다.

### 트렌드 삭제
```
DELETE /api/v1/trends/{id}
```
지정된 ID의 트렌드를 삭제합니다.

## 애플리케이션 확장

### 새 도메인 엔티티 추가

1. `domain/{entity}/vo` 패키지에 새 값 객체를 생성합니다.
2. `inbound/mysql/entity` 패키지에 새 JPA 엔티티를 생성합니다.
3. `inbound/mysql/repository` 패키지에 새 리포지토리를 생성합니다.
4. `domain/{entity}/application` 패키지에 새 서비스를 생성합니다.
5. `outbound/dto` 패키지에 새 DTO를 생성합니다.
6. `outbound/rest` 패키지에 새 리소스를 생성합니다.

### 기존 엔티티에 새 기능 추가

1. 서비스 클래스에 새 메서드를 추가합니다.
2. 리소스 클래스에 새 메서드를 추가합니다.
3. 필요한 경우 새 DTO를 추가합니다.

## 모범 사례

### 값 객체
도메인 개념을 표현하기 위해 값 객체를 사용하세요. 이는 타입 안전성과 캡슐화를 제공합니다.

예시:
```kotlin
class TrendId(value: Long) : DelegatedValue<Long>(value)
```

### 코루틴
성능과 확장성을 향상시키기 위해 비동기 작업에 Kotlin 코루틴을 사용하세요.

예시:
```kotlin
suspend fun getTrend(id: TrendId): Trend {
    return withContext(Dispatchers.IO) {
        trendJpaRepository.findByIdOrNull(id.value)
    } ?: throw NoSuchElementException("Trend not found with id: ${id.value}")
}
```

### 트랜잭션 관리
데이터 일관성을 보장하기 위해 데이터베이스 작업에 Spring의 트랜잭션 템플릿을 사용하세요.

예시:
```kotlin
txTemplates.writer.executes {
    trendJpaRepository.save(trend)
}
```

### 응답 래핑
일관된 형식으로 응답을 래핑하기 위해 확장 함수를 사용하세요.

예시:
```kotlin
trendService.createTrend(request)
    .let { trend: Trend -> TrendResponse.from(trend) }
    .wrapOk()
```

### 페이지네이션
효율적인 데이터 검색을 위해 Spring Data의 페이지네이션 지원을 사용하세요.

예시:
```kotlin
suspend fun getTrends(pageRequest: PageRequest): Page<Trend> {
    return withContext(Dispatchers.IO) {
        trendJpaRepository.findAll(pageRequest.toDefault())
    }
}
```

### 오류 처리
오류를 처리하고 의미 있는 오류 메시지를 제공하기 위해 예외를 사용하세요.

예시:
```kotlin
val trend: Trend = trendJpaRepository.findByIdOrNull(id.value)
    ?: throw NoSuchElementException("Trend not found with id: ${id.value}")
```

### Swagger 문서화
API 엔드포인트를 문서화하기 위해 Swagger 어노테이션을 사용하세요.

예시:
```kotlin
@Operation(summary = "트렌드 생성")
@PostMapping("/api/v1/trends")
suspend fun createTrend(
    @RequestBody request: TrendCreateRequest
): ResponseEntity<Response<TrendResponse>> {
    return trendService.createTrend(request)
        .let { trend: Trend -> TrendResponse.from(trend) }
        .wrapOk()
}
```
