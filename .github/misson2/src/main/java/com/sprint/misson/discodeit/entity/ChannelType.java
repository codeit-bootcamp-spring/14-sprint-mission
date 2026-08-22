package com.sprint.misson.discodeit.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ChannelType {
    PUBLIC("공개 채널"),
    PRIVATE("비공개 채널");

    String description;
}
