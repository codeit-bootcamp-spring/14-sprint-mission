package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {


    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 채널을 찾을 수 없습니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메세지를 찾을 수 없습니다."),

    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "ReadStatus를 찾을 수 없습니다."),
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "UserStatus를 찾을 수 없습니다."),
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "BinaryContent를 찾을 수 없습니다."),


    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "이미 사용 중인 userName입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 email입니다."),
    DUPLICATE_USER_STATUS(HttpStatus.CONFLICT, "이미 해당 유저의 UserStatus가 존재합니다."),
    DUPLICATE_READ_STATUS(HttpStatus.CONFLICT, "이미 해당 유저와의 채널의 ReadStatus가 존재합니다."),

    INVALID_CHANNEL_NAME(HttpStatus.BAD_REQUEST, "입력된 채널명이 없습니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "입력된 닉네임이 없습니다."),
    INVALID_MESSAGE_CONTENT(HttpStatus.BAD_REQUEST, "입력된 내용이 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST, "PRIVATE 채널은 수정할 수 없습니다."),

    INVALID_CREDENTIAL(HttpStatus.UNAUTHORIZED, "Username 또는 password가 일치하지 않습니다."),


    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public String getMessage() {
        return this.message;
    }


    }
