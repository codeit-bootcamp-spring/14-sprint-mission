package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionType {
    USER_NOT_FOUND_IN_DATABASE("User 미존재", "Database에서 해당 User를 찾을 수 없음.", "등록되지 않은 유저입니다.");

    String title;
    String description;
    String response;
}
