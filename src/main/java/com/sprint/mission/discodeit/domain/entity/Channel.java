package com.sprint.mission.discodeit.domain.entity;

import com.sprint.mission.discodeit.global.exception.CustomErrorCode;
import com.sprint.mission.discodeit.global.exception.CustomException;
import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;


@ToString
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class Channel implements Serializable, IdMapper {
    private static final long serialVersionUID = 1L;

    @Builder.Default
    UUID id = UUID.randomUUID();

    String channelName;
    ChannelType channelType;    //생성 시점에 만들어지는 필수값으로 변경 디폴트값 삭제
    String description;

    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();

    static public Channel init(String channelName, ChannelType channelType, String description){
        return Channel.builder()
            .channelName(channelName).channelType(channelType).description(description).build();
    }

    public void updateChannelNameDescription(String channelName, String description){
        validUpdatable();
        this.channelName = channelName;
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public boolean isPrivate(){
        return this.channelType == ChannelType.PRIVATE_CHANNEL;
    }

    private void validUpdatable(){
        if(isPrivate()){
            throw new CustomException(CustomErrorCode.CHANNEL_PRIVATE_CANT_UPDATE);
        }
    }
}
