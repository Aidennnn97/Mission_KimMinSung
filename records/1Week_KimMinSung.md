# 1Week_김민성.md

## Title: [1Week] 김민성

### 미션 요구사항 분석 & 체크리스트

---
- [x] 필수미션 - 호감상대 삭제
  * 배경
    * 현재 호감목록 기능까지 구현되어 있다.
      * 호감목록 페이지에서는 여태까지 본인이 호감을 표시한 상대방의 목록을 볼 수 있다.
    * 현재 삭제버튼까지 구현되어 있다.
  * 목표
    * 호감목록 페이지에서 특정 항목에서 삭제버튼을 누르면, 해당 항목은 삭제되어야 한다.
      * 삭제를 처리하기 전에 해당 항목에 대한 소유권이 본인(로그인한 사람)에게 있는지 체크해야 한다.
    * 삭제 후 다시 호감목록 페이지로 돌아와야 한다.
      * rq.redirectWithMsg 함수 사용

  **SQL**
  ```sql
  # 5번 항목의 삭제버튼을 눌렀을 경우에 실행되어야 하는 SQL
  DELETE
  FROM likeable_person
  WHERE id = 5;
  ```

- [x] 선택미션 - 구글 로그인
  * 배경
    * 현재 일반 로그인과 카카오 로그인까지 구현되어 있다.
      * 일반 회원의 providerTypeCode : GRAMGRAM
      * 카카오 로그인으로 가입한 회원의 providerTypeCode : KAKAO
      * 스프링 OAuth2 클라이언트로 구현되어 있다.
      * 카카오 개발자 도구에서 앱 등록, 앱으로 부터 앱키(REST API)를 받아서 프로젝트에 삽입하는 과정이 선행되었음
    * 구글 로그인도 카카오 로그인 구현과정을 그대로 따라하면 된다.
  * 목표
    * 카카오 로그인이 가능한것 처럼, 구글 로그인으로도 가입 및 로그인 처리가 되도록 해주세요.
      * 스프링 OAuth2 클라이언트로 구현해주세요.
    * 구글 로그인으로 가입한 회원의 providerTypeCode : GOOGLE

  **SQL**
  ```sql
  # 최초로 구글 로그인을 통해서 가입이 될 때 실행되어야 할 SQL
  # 구글 앱에서의 해당 회원번호를 2731659195 라고 가정
  INSERT INTO `member`
  SET create_date = NOW(),
  modify_date = NOW(),
  provider_type_code = 'GOOGLE',
  username = 'GOOGLE__2731659195',
  password = '',
  insta_member_id = NULL;
  ```

### 1주차 미션 요약

---

**[접근 방법]**

1. 필수미션, 호감상대가 삭제되는 과정을 파악했다
> 뷰에서 온 삭제요청을 컨트롤러에서 받아 서비스에서 처리하도록 넘겨주는 흐름으로 접근하여 개발을 진행했다.
2. 선택미션, 구글 로그인
> 카카오 로그인이 구현된 코드를 본 후 똑같이 진행 했으며 [구글 OAuth 사용방법 블로그](https://velog.io/@shawnhansh/Spring-Security%EC%99%80-OAuth22-%EA%B5%AC%EA%B8%80-%EC%84%9C%EB%B9%84%EC%8A%A4-%EB%93%B1%EB%A1%9D%ED%95%98%EA%B3%A0-%EC%A0%81%EC%9A%A9%ED%95%98%EA%B8%B0)를 참고했다.

**[특이사항]**
> 구글 로그인을 추가하는 과정에서 카카오로그인과 다르게 application.yml 파일의 oauth2 - client - provider 부분이 없어 찾아본 결과
> 
> 스프링부트 2.0 부터는 CommonOAuth2 Provider라는 enum이 추가되어 Google, Github, Facebook, Okta의 기본설정 값들은 모두 제공되기 때문에 
> 
> client에 관련된 정보만 입력해 줘도 된다. 하지만 네이버, 카카오는 스프링에서 지원해 주지 않기 때문에 입력해줘야 하는 값들이 많다.