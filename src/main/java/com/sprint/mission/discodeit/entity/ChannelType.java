package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ChannelType {
    PUBLIC("공개 채널"),
    PRIVATE("비공개 채널");

    private final String value;
}
