package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class User {
    // 공통 필드
    private UUID id;        // 객체를 식별하기 위한 id
    private Long createdAt; // 객체의 생성 시간을 기록
    private Long updatedAt; // 객체의 수정 시간을 기록

    // 추가 필드
    private String nickName;
    private String email;

    // 생성자 : 공통 필드인 id, createdAt, updatedAt은 생성자 내부에서 직접 초기화
    // 추가 필드인 nickName, email은 생성자 파라미터로 초기화
    public User(String nickName, String email) {
        this.id = UUID.randomUUID();                 // 랜덤으로 중복 없는 고유 ID 생성
        this.createdAt = System.currentTimeMillis(); // 현재 시간을 숫자로 저장
        this.updatedAt = System.currentTimeMillis(); // 수정 시간을 숫자로 다시 저장
        this.nickName = nickName;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }
    public Long getCreatedAt() {
        return createdAt;
    }
    public Long getUpdatedAt() {
        return updatedAt;
    }
    public String getNickName() {return nickName;}
    public String getEmail() {return email;}

    // 필드를 수정하는 update 함수 (안전한 setter)
    public void update(String nickName, String email) {
        this.nickName = nickName;
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }
}
