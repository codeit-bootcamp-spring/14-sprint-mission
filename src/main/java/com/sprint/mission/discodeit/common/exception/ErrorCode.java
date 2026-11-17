package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 존재하는 유저이름입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_USER_INPUT(HttpStatus.BAD_REQUEST, "사용자 입력값이 올바르지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 일치하지 않습니다."),

    // user status
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자 상태를 찾을 수 없습니다."),
    DUPLICATE_USER_STATUS(HttpStatus.CONFLICT, "이미 사용자 상태가 존재합니다."),

    // channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE_NOT(HttpStatus.FORBIDDEN, "Private 채널은 수정할 수 없습니다."),

    // message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메시지를 찾을 수 없습니다."),

    // binary content
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일을 찾을 수 없습니다."),

    // read status
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 읽음 상태를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS(HttpStatus.CONFLICT, "이미 읽음 상태가 존재합니다.");

    private final HttpStatus status;
    private final String message;
}
