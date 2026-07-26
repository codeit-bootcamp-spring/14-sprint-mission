package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message {
    // 공통 필드
    private UUID id;        // 객체를 식별하기 위한 id
    private Long createdAt; // 객체의 생성 시간을 기록
    private Long updatedAt; // 객체의 수정 시간을 기록

    // 추가 필드
    private String content; // 메세지 내용

    // 심화 추가 필드
    private UUID userId;
    private UUID channelId;

    // 생성자 : 공통 필드인 id, createdAt, updatedAt은 생성자 내부에서 직접 초기화
    // 추가 필드인 content 는 생성자 파라미터로 초기화
    public Message(String content, UUID userId, UUID channelId) {
        this.id = UUID.randomUUID();
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
        this.content = content;
        this.userId = userId;
        this.channelId = channelId;
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
    public String getContent() {return content;}
    public UUID getUserId() {
        return userId;
    }
    public UUID getChannelId() {
        return channelId;
    }

    // 필드를 수정하는 update 함수 (수정 시간 갱신) - id, 생성시간은 수정할 필요없음
    public void update(String content) {
        this.content = content;
        this.updatedAt = System.currentTimeMillis();
    }
}
