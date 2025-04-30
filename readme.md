# GraphQL Flux Board

Spring WebFlux 및 GraphQL을 활용한 리액티브 게시판 API입니다.  
Reactive MongoDB와 Project Reactor 기반으로 비동기 처리를 학습하며,  
GraphQL의 Query/Mutation/BatchMapping 구조를 중심으로 CRUD 기능을 구현했습니다.

## 🧠 기술 스택

- Spring Boot 3
- Spring WebFlux
- Spring GraphQL
- Reactive MongoDB
- Project Reactor
- Jakarta Bean Validation

## 📦 모듈 구성
```
com.example.graphqlfluxboard  
├── comment         - 댓글 도메인  
├── post            - 게시글 도메인  
├── reply           - 대댓글(답글) 도메인  
├── user            - 사용자 도메인  
└── common          - 공통 예외처리 및 유틸
```
## ⚙️ 주요 기능

- 게시글 CRUD
- 댓글 및 대댓글 CRUD
- 사용자 회원가입 / 삭제
- 비밀번호 인증 기반 삭제 및 작성
- 사용자 정보 조회
- 댓글, 대댓글의 작성자 정보와 관계된 글 조회

## 📂 디렉토리 설명

- resolver: GraphQL 쿼리/뮤테이션 처리용 컨트롤러
- service: 비즈니스 로직
- domain: MongoDB 도큐먼트 객체
- dto: 입력값 처리용 DTO
- repos: MongoRepository 인터페이스
- common.exception: 예외 및 에러코드 처리
- common.validation: Validation 관련 클래스

## ❌ 예외 처리

- 인증 실패: AuthException
- 리소스 없음: NotFound
- 중복: DuplicateException
- 지원하지 않는 정렬 필드: NotSupport

모든 예외는 GlobalGraphQLExceptionHandler에서 공통 처리됩니다.

## 📄 GraphQL 스키마 요약

- type Query:
    - posts, post
    - comments, comment, commentsByPostId
    - replies, reply
    - users, user

- type Mutation:
    - 게시글/댓글/대댓글/사용자 생성 및 삭제
