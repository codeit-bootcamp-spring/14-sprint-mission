package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionType {
    INPUT_IS_NULL("입력값이 비어있음", "빈 문자열을 입력함", "입력값이 비어있습니다"),

    // Repository 관련

    USER_NOT_FOUND("User 없음", "User 조회중 발생 - User ID: ", "존재하지 않는 User입니다."),
    CHANNEL_NOT_FOUND("Channel 없음", "Channel 조회중 발생 - Channel ID: ", "존재하지 않는 Channel입니다."),
    MESSAGE_NOT_FOUND("Message 없음", "Message 조회중 발생 - Message ID: ", "존재하지 않는 Message입니다."),

    USER_ALREADY_EXISTS("User 이미 존재함", "User 생성중 발생 - User ID: ", "이미 존재하는 User입니다."),
    CHANNEL_ALREADY_EXISTS("Channel 이미 존재함", "Channel 생성중 발생 - Channel ID: ", "이미 존재하는 Channel입니다."),
    MESSAGE_ALREADY_EXISTS("Message 이미 존재함", "Message 생성중 발생 - Message ID: ", "이미 존재하는 Message입니다."),

    USER_REPO_IS_NULL("불러올 User가 없음", "User 호출중 발생", "저장된 User가 없습니다."),
    CHANNEL_REPO_IS_NULL("불러올 User가 없음", "User 호출중 발생", "저장된 User가 없습니다."),
    MESSAGE_REPO_IS_NULL("불러올 User가 없음", "User 호출중 발생", "저장된 User가 없습니다.");

    String detail;
    String description;
    String response;
}
