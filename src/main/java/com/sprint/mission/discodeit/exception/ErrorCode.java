package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND("유저를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 유저입니다."),
    USER_STATUS_NOT_FOUND("유저 상태 정보를 찾을 수 없습니다."),
    DUPLICATE_USER_STATUS("이미 존재하는 유저 상태입니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("PRIVATE 채널은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    INVALID_CREDENTIALS("아이디 또는 비밀번호가 올바르지 않습니다."),
    READ_STATUS_NOT_FOUND("읽음 상태 정보를 찾을 수 없습니다."),
    DUPLICATE_READ_STATUS("이미 존재하는 읽음 상태입니다."),
    BINARY_CONTENT_NOT_FOUND("파일을 찾을 수 없습니다.");


    private final String message;
}
