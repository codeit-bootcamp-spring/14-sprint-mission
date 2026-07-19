package com.sprint.mission.discodeit.enumType;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum userRole {
    ADMINISTRATOR("관리자"),
    MANAGER("매니저"),
    TEAMLEADER("팀장"),
    JUNIOR("신입");

    String roleName;
}
