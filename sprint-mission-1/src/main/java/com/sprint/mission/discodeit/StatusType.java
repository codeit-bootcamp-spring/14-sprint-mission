package com.sprint.mission.discodeit;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum StatusType {
    SEVER("서버"),
    CATEGORY("카테고리"),
    CHANNEL("채널");

    String statusName;
}
