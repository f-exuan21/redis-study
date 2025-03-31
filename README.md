# Redis_Project

## 1. Multi Module
### 1. API 모듈 (Application, Presentation)   
- 클라이언트와 도메인 사이의 데이터를 전달하고, 사용자의 요청을 적절히 처리합니다.
- Application 에서 사용하는 RequestDto, ResponseDto 를 Presentation 계층이 사용할 수 있도록 하기 위해 하나의 모듈로 합쳤습니다. 
- 둘 사이에 DTO를 둘 수도 있겠지만, 더 복잡해질 것 같아 하나의 모듈로 합쳤습니다.  

### 2. Domain 모듈 (Domain)  
- 핵심 비즈니스 로직을 가지며, 순수한 비즈니스 로직만 가지고 있습니다. 
### 3. Infrastructure 모듈 (Infrastructure)  
- 외부 시스템과의 통신, 데이터 저장 및 처리를 전담합니다.  
- Application Layer에서 JPA를 사용한 Infrastructure Layer를 의존하지 않도록 하기 위해 
Domain 모듈에 정의해 놓은 Repository 인터페이스로 Adapter 를 구현하였습니다.  
## 2. Table Design
![readme-img/img.png](readme-img/img.png)
- 좌석은 모든 상영관이 똑같다고 가정합니다.
- 영화장르나 좌석은 추후에 변경 및 추가될 수 있다고 가정하고 테이블을 생성했습니다.
- 영상물 등급은 잘 변하지 않는 값인 것으로 보여, 테이블을 따로 생성하지 않았습니다.

## 3. API 
- 영화 상영 목록 조회 : [GET] /movie  

## 4. 예시 페이지
![readme-img/img_1.png](readme-img/img_1.png)

## 5. 부하 테스트 
[부하테스트](./부하테스트.md)

## 6. 분산락 테스트
![Pasted Graphic.png](..%2F..%2F..%2FLibrary%2FGroup%20Containers%2Fgroup.com.apple.notes%2FAccounts%2F547DBCAB-EC86-4B59-AA5B-C9548915CDD6%2FMedia%2F87036087-6E2F-4483-81C6-56EBD006B36B%2F1_CE196611-2D61-4292-8073-8DE351F4DB38%2FPasted%20Graphic.png)
![1 testen.png](..%2F..%2F..%2FLibrary%2FContainers%2Fcom.apple.Notes%2FData%2Ftmp%2FTemporaryItems%2FNSIRD_%EB%A9%94%EB%AA%A8_4gL9Kv%2FHardLinkURLTemp%2F92034EF6-D2CE-4548-88FE-2B51D1F697A4%2F1743317032%2F1%20testen.png)
아무것도 적용하지 않았을 때, 같은 영화 같은 좌석에 예매를 성공함

![Pasted Graphic 2.png](..%2F..%2F..%2FLibrary%2FGroup%20Containers%2Fgroup.com.apple.notes%2FAccounts%2F547DBCAB-EC86-4B59-AA5B-C9548915CDD6%2FMedia%2FF5824959-C68C-4541-8652-2CF388489C49%2F1_638C5DA1-2A76-43FE-B047-3CD8AC386A43%2FPasted%20Graphic%202.png)
분산락을 적용한 이후 정상적으로 예약이 되는 것을 확인

### AOP 분산 락 테스트 결과 

### AOP 분산 락 (MultiLock) 테스트 결과 

### 함수형 분산 락 테스트 결과 

