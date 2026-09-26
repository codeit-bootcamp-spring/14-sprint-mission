package com.sprint.mission.discodeit.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    //user
    DUPLICATE_USERNAME(HttpStatus.CONFLICT,"이미 존재하는 유저이름입니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    NO_SUCH_ELEMENT(HttpStatus.NOT_FOUND, "해당 값이 존재하지 않습니다."),
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED,  "아이디 또는 비밀번호가 일치하지 않습니다."),
    PRIVATE_CHANNEL_UPDATE_NOT(HttpStatus.FORBIDDEN,  "Private 채널은 수정할 수 없습니다."),
    NOT_FOUND_CHANNEL(HttpStatus.NOT_FOUND,  "해당 채널을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND,  "해당 유저를 찾을 수 없습니다."),
    DUPLICATE_USER_ID(HttpStatus.CONFLICT,  "이미 존재하는 유저입니다."),
    DUPLICATE_CHANNEL_ID(HttpStatus.CONFLICT,  "이미 존재하는 채널입니다."),
    DUPLICATE_STATUS(HttpStatus.CONFLICT,  "해당 값이 이미 존재합니다.");

    private final HttpStatus status;
    private final String message;
}
