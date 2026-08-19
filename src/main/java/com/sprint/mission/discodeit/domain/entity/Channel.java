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

    @Builder.Default
    Instant createdAt = Instant.now();
    @Builder.Default
    Instant updatedAt = Instant.now();

    static public Channel init(String channelName, ChannelType channelType){
        return Channel.builder()
            .channelName(channelName).channelType(channelType).build();
    }

    public void updateChannelName(String channelName){
        this.channelName = channelName;
        this.updatedAt = Instant.now();
    }

    public boolean isPrivate(){
        return this.channelType == ChannelType.PRIVATE_CHANNEL;
    }

    public void validUpdatable(){
        if(isPrivate()){
            throw new CustomException(CustomErrorCode.CHANNEL_PRIVATE_CANT_UPDATE);
        }
    }
}
