package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Channel {
    // 공통 필드
    private UUID id;        // 객체를 식별하기 위한 id
    private Long createdAt; // 객체의 생성 시간을 기록
    private Long updatedAt; // 객체의 수정 시간을 기록

    // 추가 필드
    private String name;        // 채널 이름
    private String description; // 채널 설명

    // 생성자 : 공통 필드인 id, createdAt, updatedAt은 생성자 내부에서 직접 초기화
    // 추가 필드인 name, description 은 생성자 파라미터로 초기화
    public Channel(String name, String description) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.name = name;
        this.description = description;
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
    public String getName() {return name;}
    public String getDescription() {return description;}

    // 필드를 수정하는 update 함수 (안전한 setter)
    public void update(String name, String description) {
        this.name = name;
        this.description = description;
        this.updatedAt = System.currentTimeMillis();
    }
}
