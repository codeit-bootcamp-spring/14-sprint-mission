package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 사용자(이메일 또는 아이디)입니다."),
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    INVALID_INPUT_VALUE("입력값이 올바르지 않습니다."),
    BINARY_CONTENT_NOT_FOUND("첨부 파일을 찾을 수 없습니다."),
    READ_STATUS_NOT_FOUND("읽음 상태 정보를 찾을 수 없습니다."),
    USER_STATUS_NOT_FOUND("사용자 상태 정보를 찾을 수 없습니다."),
    USER_STATUS_ALREADY_EXISTS("해당 사용자의 상태 정보가 이미 존재합니다.");

    private final String message;
}
