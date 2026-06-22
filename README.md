# 🍃 프로젝트 개요: ipms-pa (Spring Boot Project)

본 프로젝트는 **Spring Boot 3.4.0**과 **Java 17**을 기반으로 구축된 지식재산권 관리 시스템(Intellectual Property Management System)   입니다.

시스템의 **보안성(JWT)**, **성능(Log4j2 Async)**, **확장성(AWS S3/NCP 연동)** 및 **문서화(Swagger)**를 최우선으로 고려하여 설계되었습니다.


---


### 🎯 주요 설계 목표
*   **Security:** JWT 기반의 견고한 인증 시스템 구축
*   **Performance:** 비동기 로깅을 통한 트래픽 처리 성능 최적화
*   **Scalability:** 클라우드 오브젝트 스토리지(S3/NCP) 연동으로 유연한 저장소 확장
*   **Documentation:** Swagger를 통한 자동화된 API 명세 및 테스트 환경 제공

---
<br>

### ⚙️ 1. 핵심 기술 스택 (Core Tech Stack)
| **Category**       | **Technology**                       |**Version / Details**|
|:-------------------|:-------------------------------------|---:|
| AI &nbsp;          | &nbsp; Spring Boot AI &nbsp;         |&nbsp; 1.0.0-M6 &nbsp; |
| Framework &nbsp;   | &nbsp; Spring Boot &nbsp;            |&nbsp; 3.4.0 &nbsp; |
| Language &nbsp;    | &nbsp; Java &nbsp;                   |&nbsp; 17 &nbsp; |
| Database &nbsp;    | &nbsp; PostgreSQL &nbsp;             |
| ORM/Mapping &nbsp; | &nbsp; MyBatis &nbsp;                |&nbsp; 3.0.4 &nbsp; |
| Security &nbsp;    | &nbsp; Spring Security & JJWT &nbsp; |&nbsp; 0.12.6 (JSON Web Token) &nbsp; |
---

<br>

### 🛡️ 보안 및 인증 (Security)
Spring Security와 최신 **JJWT** 라이브러리를 결합하여 **Stateless 인증 시스템**을 구현했습니다. 
* **비밀번호 보안:** 사용자 비밀번호 암호화 및 안전한 저장.
* **권한 관리:** 토큰 기반 권한 관리를 통해 정밀한 API 접근 제어 및 안전한 서비스 환경 제공.

<br>

### 📂 파일 스토리지 (Storage Strategy)
`software.amazon.awssdk:s3` 라이브러리를 채택하여 **AWS S3** 및 **네이버 클라우드(NCP) Object Storage**와 완벽하게 호환되는 파일 업로드/삭제 기능을 구현했습니다. 
* **유연한 환경 구성:** `@ConditionalOnProperty` 설정을 활용하여 설정값 하나로 **로컬 저장소**와 **클라우드 저장소**를 자유롭게 전환(Switching)할 수 있는 구조입니다.
* **멀티 클라우드 지원:** 표준 S3 프로토콜을 준수하여 다양한 클라우드 스토리지 서비스로 확장 가능합니다.

<br>

### 📈 로깅 시스템 (Advanced Logging)
기본 로깅 시스템(Logback) 대신 **Log4j2**를 전역 로깅 엔진으로 채택하여 시스템 안정성을 확보했습니다.
* **비동기 로깅 (Async Logging):** **Lmax Disruptor(3.4.4)**를 도입하여 대량의 트래픽 상황에서도 애플리케이션의 입출력(I/O) 성능 저하를 최소화했습니다.
* **SQL 모니터링:** `log4jdbc-log4j2`를 연동하여 실행되는 모든 SQL 쿼리를 정렬된 상태로 실시간 모니터링할 수 있습니다.

<br>

### 📖 API 문서화 및 테스트 (Documentation)
* **Swagger / OpenAPI 3:** `springdoc-openapi-ui`를 통해 최신 API 명세서를 자동으로 생성하며, 별도의 도구 없이 UI 상에서 직접 API 테스트가 가능합니다.
* **Data Validation:** `jakarta.validation`을 사용하여 DTO(Data Transfer Object) 레벨에서 강력한 데이터 검증을 수행함으로써 데이터 무결성을 보장합니다.

---

<br>
<br>

|**구분**|**기술 라이브러리**|**용도**|
|:---|:---|:---|
|Web &nbsp;&nbsp;|Spring Web, Validation|&nbsp;&nbsp;&nbsp;REST API 구현 및 데이터 검증|
|Security &nbsp;&nbsp;|Spring Security, JJWT|&nbsp;&nbsp;&nbsp;JWT 기반 인증 및 인가 제어|
|Storage &nbsp;&nbsp;|AWS SDK v2 (S3)|&nbsp;&nbsp;&nbsp;네이버 클라우드 Object Storage 연동|
|Data &nbsp;&nbsp;|MyBatis, PostgreSQL|&nbsp;&nbsp;&nbsp;관계형 데이터베이스 매핑 및 영속성 관리|
|Logging &nbsp;&nbsp;|Log4j2, Disruptor|&nbsp;&nbsp;&nbsp;고성능 비동기 로그 처리|
|Utility &nbsp;&nbsp;|Lombok, Jackson, JAXB|&nbsp;&nbsp;&nbsp;코드 간소화 및 데이터 직렬화(JSON/XML/YAML)|

---

### 📖 라이센스 (License)


### 📖 관련 문의

https://www.mindpro.co.kr

info@mindpro.co.kr
