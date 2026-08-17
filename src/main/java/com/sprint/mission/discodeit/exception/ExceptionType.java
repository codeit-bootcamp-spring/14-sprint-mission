package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

/**
 * 예외 하나하나를 클래스로 만드는 대신, 상태 코드와 기본 메시지를 enum 값으로 묶음.
 * 예외가 늘어나도 GlobalExceptionHandler의 분기문은 늘지 않는다 — type에서 바로 꺼내 쓰면 된다.
 */
public enum ExceptionType {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 username입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 email입니다."),

    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_REQUIRES_PARTICIPANTS(HttpStatus.BAD_REQUEST, "비공개 채널은 참여자가 최소 1명 필요합니다."),

    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "메시지를 찾을 수 없습니다."),

    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 상태를 찾을 수 없습니다."),
    USER_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 상태가 등록된 유저입니다."),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "읽음 상태를 찾을 수 없습니다."),
    READ_STATUS_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 유저·채널 조합입니다."),

    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "파일을 찾을 수 없습니다."),

    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "아이디 또는 비밀번호가 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ExceptionType(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
