package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.ChannelType;
import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequestDto;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class Channel extends Entity implements Serializable {
    private String name;
    private ChannelType channelType;

    // ChannelCreateDto를 통해 Channel 객체 생성
    private Channel(String name, ChannelType channelType) {
        super();
        this.name = name;
        this.channelType = channelType;
    }

    // Channel 객체를 생성하는 정적 팩토리 메소드
    public static Channel from(ChannelCreateRequestDto requestDto) {
        return new Channel(
                requestDto.getName(),
                requestDto.getChannelType()
        );
    }

    public void update(String name, ChannelType channelType) {
        boolean isUpdated = false;

        if (name != null && !name.equals(this.name)) {
            isUpdated = true;
            this.name = name;
        }
        if (channelType != null && !channelType.equals(this.channelType)) {
            isUpdated = true;
            this.channelType = channelType;
        }

        if (isUpdated) {
            updateTimestamp();
        }
    }
}
