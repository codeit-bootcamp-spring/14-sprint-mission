package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionType {
    // NOT_FOUND_IN_DATABASE
    USER_NOT_FOUND_IN_DATABASE("User 미존재", "Database에서 해당 User를 찾을 수 없음.", "등록되지 않은 User입니다."),
    USERSTATUS_NOT_FOUND_IN_DATABASE("UserStatus 미존재", "Database에서 해당 UserStatus 찾을 수 없음", "등록되지 않은 UserStatus입니다."),
    CHANNEL_NOT_FOUND_IN_DATABASE("Channel 미존재", "Database에서 해당 Channel 찾을 수 없음", "등록되지 않은 Channel입니다."),
    MESSAGE_NOT_FOUND_IN_DATABASE("Message 미존재", "Database에서 해당 Message 찾을 수 없음", "등록되지 않은 Message입니다."),

    // NO_ACCESS
    NO_ACCESS_TO_CHANNEL("Channel 권한 없음", "등록되지 않은 User는 PRIVATE채널에 접근할 수 없습니다", "Channel에 접근할 수 없습니다."),

    // UNIQUE_FIELD_CONFLICT
    USER_UNIQUE_FIELD_CONFLICT("User 필드 중복", "Name, Email 필드 중복 불가", "이미 사용 중인 name, email입니다.");

    String title;
    String description;
    String response;
}
