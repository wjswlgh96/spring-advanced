# SPRING ADVANCED

***

## Lv.5 리팩토링

1. Interceptor 기능 구현 후 Filter 기능과 겹치는 문제 인지


2. Interceptor 에 Admin 기능 로그를 기록하면서 JwtUtil 도 함께 사용해서 Filter 제거 후 Interceptor 로 통합
   
   2-1. Interceptor 에서 로깅이 좀 더 자유로웠기에 Interceptor 에 통합 결정

   2-2. 모든 기능은 그대로 유지하면서 throw error 까지 사용할 수 있어 GlobalException 까지 사용 가능


3. 해결 완료

   3-1. 작업은 매우 간단했고, 기존 httpResponse로 sendError 해주던 부분도 바로 예외를 던져주어 편해졌다.

   3-2. 포스트맨 정상 작동 확인, 테스트코드 정상작동 확인 기능 이상 없음 점검 완료  

***

## Lv.6 테스트 커버리지

### ✅ PasswordEncoderTest

![Image](https://github.com/user-attachments/assets/e086a1ed-411c-48d8-b4a7-de933e151d08)

### ✅ CommentTest

#### CommentControllerTest
![Image](https://github.com/user-attachments/assets/414cf846-3800-4c26-87e2-72c2f0e5398b)

#### CommentRepositoryTest
![Image](https://github.com/user-attachments/assets/de013248-7ecf-4853-a2ed-1c5c285b873f)

#### CommentServiceTest

![Image](https://github.com/user-attachments/assets/9cc1a095-1c46-4a27-ac74-c1d8d5f93bfb)

### ✅ ManagerServiceTest

![Image](https://github.com/user-attachments/assets/24e0967b-c7a1-48b2-a6a8-a69b5043b6c6)