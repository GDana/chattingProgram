# chattingProgram
socket을 이용한 채팅 프로그램

![채팅움짤](https://user-images.githubusercontent.com/35926413/86130181-bfd45c80-bb1e-11ea-917a-ffb829e20577.gif)

<br>

## 개발 목표

- [ ] Socket과 Thread 사용을 이해한다.
- [ ] 각 메서드가 어떤 역할을 하는지 이해한다.
- [ ] `try01` 프로젝트를 기준으로 `try02`, `try03`.. 늘려가며 안보고 구현할 수 있을때까지 온전히 이해한다.

## 기능 구현 목표

- [ ] 접속자 구분하여 표시
- [x] 채팅 내용 Oracle DB저장

## SQL
```sql
-- 1. 시스템 관리자 로그인
conn sys/sys12345 as sysdba;

-- 2. 유저 생성
create user chatting identified by chatting;

-- 3. 유저 권한 부여
grant connect, dba, resource to chatting;

-- 4. 유저 로그인
conn chatting/chatting;

-- 5. try02 테이블 생성
CREATE TABLE try02(
	ip number(30) NOT NULL,
	chat varchar(100) NOT NULL
);
```
