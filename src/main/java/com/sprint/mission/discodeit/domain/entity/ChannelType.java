package com.sprint.mission.discodeit.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
@AllArgsConstructor
public enum ChannelType {
    PRIVATE_CHANNEL("프라이빗채널"),
    PUBLIC_CHANNEL("퍼블릭채널");

    private final String channelType;
}
