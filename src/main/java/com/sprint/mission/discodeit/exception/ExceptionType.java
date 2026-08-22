package com.sprint.mission.discodeit.exception;

import java.net.HttpURLConnection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionType {
    USER_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "존재하지 않는 유저입니다."
    ),
    CHANNEL_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "존재하지 않는 채널입니다."
    ),
    MESSAGE_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "존재하지 않는 메세지입니다."
    ),
    READSTATUS_NOT_FOUND(
        Level.WARN,
        HttpURLConnection.HTTP_NOT_FOUND,
        "잘못된 요청입니다."
    ),
    USER_ALREADY_EXIST(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 존재하는 유저입니다."
    ),
    CHANNEL_ALREADY_EXIST(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 존재하는 채널입니다."
    ),
    MESSAGE_ALREADY_EXIST(
        Level.WARN,
        HttpURLConnection.HTTP_CONFLICT,
        "이미 존재하는 메세지입니다."
    ),
    INVALID_INFO(
        Level.WARN,
        HttpURLConnection.HTTP_UNAUTHORIZED,
        "이름과 비밀번호를 다시 확인해주세요."
    );
    Level level;
    int status;
    String message;
}
