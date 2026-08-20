package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionType {
    // Auth
    LOGIN_FAILED(HttpStatus.BAD_REQUEST, "로그인 실패", "잘못된 username 혹은 password 입니다.", "잘못된 username 혹은 password 입니다."),

    // NOT_FOUND_IN_DATABASE
    USER_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "User 미존재", "Database에서 해당 User를 찾을 수 없음.", "등록되지 않은 User입니다."),
    USERSTATUS_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "UserStatus 미존재", "Database에서 해당 UserStatus 찾을 수 없음", "등록되지 않은 UserStatus입니다."),
    CHANNEL_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "Channel 미존재", "Database에서 해당 Channel 찾을 수 없음", "등록되지 않은 Channel입니다."),
    MESSAGE_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "Message 미존재", "Database에서 해당 Message 찾을 수 없음", "등록되지 않은 Message입니다."),
    READSTATUS_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "ReadStatus 미존재", "Database에서 해당 ReadStatus 찾을 수 없음", "등록되지 않은 ReadStatus입니다."),

    // NO_ACCESS
    NO_ACCESS_TO_CHANNEL(HttpStatus.BAD_REQUEST, "Channel 권한 없음", "등록되지 않은 User는 PRIVATE채널에 접근할 수 없습니다", "Channel에 접근할 수 없습니다."),

    // CANNOT_MODIFY
    CHANNEL_CANNOT_BE_MODIFIED(HttpStatus.BAD_REQUEST, "채널 정보 수정 불가", "PRIVATE 채널의 정보는 수정할 수 없습니다", "PRIVATE 채널의 정보는 수정할 수 없습니다."),

    // 이거 두개는 뭔가 비슷한듯 다른듯 접미사를 통일할지 말지 잘 모르겠음
    // ALREADY_EXISTS
    USERSTATUS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "UserStatus 중복", "해당 UserStatus 이미 존재", "이미 등록된 UserStatus입니다."),
    READSTATUS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "ReadStatus 중복", "해당 ReadStatus 이미 존재", "이미 등록된 ReadStatus입니다."),
    // UNIQUE_FIELD_CONFLICT
    USER_UNIQUE_FIELD_CONFLICT(HttpStatus.BAD_REQUEST, "User 필드 중복", "Name, Email 필드 중복 불가", "이미 사용 중인 name, email입니다.");

    HttpStatus httpStatus;
    String title;
    String description;
    String response;
}
