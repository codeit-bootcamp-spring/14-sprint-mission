package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {
    ADMIN("관리자 계정입니다."),
    CHANNEL_MANAGER("채널 매니저 계정입니다."),
    USER("일반 유저 계정입니다.");

    private final String description;
}
