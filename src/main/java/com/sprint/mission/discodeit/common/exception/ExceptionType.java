package com.sprint.mission.discodeit.common.exception;

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

    // NOT_FOUND
    USER_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "User 미존재", "Database에서 해당 User를 찾을 수 없음.", "등록되지 않은 User입니다."),
    USERSTATUS_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "UserStatus 미존재", "Database에서 해당 UserStatus 찾을 수 없음", "등록되지 않은 UserStatus입니다."),
    CHANNEL_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "Channel 미존재", "Database에서 해당 Channel 찾을 수 없음", "등록되지 않은 Channel입니다."),
    MESSAGE_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "Message 미존재", "Database에서 해당 Message 찾을 수 없음", "등록되지 않은 Message입니다."),
    READSTATUS_NOT_FOUND_IN_DATABASE(HttpStatus.NOT_FOUND, "ReadStatus 미존재", "Database에서 해당 ReadStatus 찾을 수 없음", "등록되지 않은 ReadStatus입니다."),

    FILE_NOT_FOUND(HttpStatus.UNPROCESSABLE_CONTENT, "비어있거나 존재하지 않는 파일", "처리해야 하는 MultipartFile이 없거나 비어 있음", "잘못된 형식의 파일입니다."),
    FILE_IO_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 실패", "파일 업로드 실패", "파일 업로드를 실패했습니다, 잠시 후에 다시 시도하세요."),

    // NO_ACCESS
    NO_ACCESS_TO_CHANNEL(HttpStatus.FORBIDDEN, "Channel 권한 없음", "등록되지 않은 User는 PRIVATE채널에 접근할 수 없습니다", "Channel에 접근할 수 없습니다."),

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
