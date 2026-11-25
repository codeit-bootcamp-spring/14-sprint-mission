package com.sprint.mission.discodeit.domain.entity;

import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;


/*
    어떤 유저가 어디 채널을 언제 마지막으로 읽었는지 체크하고
    이로 그 이후의 메시지는 읽지않음으로 표시하기 위한 엔터티
 */
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder(access = AccessLevel.PRIVATE)                  //어디서든 new 로 생성안하고 반드시 메서드로만 생성하는거로 변경
public class ReadStatus implements IdMapper{
    @Builder.Default
    UUID id = UUID.randomUUID();
    UUID userId;
    UUID channelId;
    Instant lastReadAt;

    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();

    //개체 생성은 무조건 메서드로 호출
    static public ReadStatus init(UUID userId, UUID channelId, Instant lastReadAt){
        return ReadStatus.builder()
            .userId(userId).channelId(channelId).lastReadAt(lastReadAt).build();
    }

    //채널 입장 / 활동 / 퇴장 시 마다 호출
    public void updateReadTime(Instant updateTime){
        this.lastReadAt = updateTime;
        this.updatedAt = Instant.now();
    }
}
