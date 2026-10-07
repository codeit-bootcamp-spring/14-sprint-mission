package com.sprint.mission.discodeit.common;

import java.net.HttpURLConnection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "유저를 찾을 수 없습니다."
    ),
    DUPLICATE_USER_NAME(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 사용중인 이름 입니다."
    ),
    CHANNEL_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "채널을 찾을 수 없습니다."
    ),
    PRIVATE_CHANNEL_UPDATE(
        Level.WARN,
        HttpURLConnection.HTTP_BAD_REQUEST,
       "업데이트할 수 없는 채널입니다."
    ),
    DUPLICATE_EMAIL(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 사용중인 이메일 입니다."
    ),
    MESSAGE_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "메세지 찾을 수 없습니다."
    ),
    USER_STATUS_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "유저 상태를 찾을 수 없습니다."
    ),
    DUPLICATE_USER_STATUS(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 유저 상태가 존재합니다."
    ),
    INVALID_PASSWORD(
        Level.WARN,
        HttpURLConnection.HTTP_BAD_REQUEST,
        "비밀번호가 일치하지 않습니다."
    ),
    READ_STATUS_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "읽음 상태를 찾을 수 없습니다."
    ),
    INCORRECT_REQUEST(
        Level.WARN,
        HttpURLConnection.HTTP_BAD_REQUEST,
        "올바른 입력이 아닙니다."
    );

    Level level;
    int status;
    String message;
}
