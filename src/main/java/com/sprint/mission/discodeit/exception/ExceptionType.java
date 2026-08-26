package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

import java.net.HttpURLConnection;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionType {
    // 404 NOT FOUND
    USER_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "User with id %s not found"
    ),
    USER_BY_USERNAME_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "User with username %s not found"
    ),
    USER_STATUS_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "UserStatus with userId %s not found"
    ),
    CHANNEL_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "Channel with id %s not found"
    ),
    MESSAGE_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "Message with id %s not found"
    ),
    READ_STATUS_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "ReadStatus with id %s not found"
    ),
    BINARY_CONTENT_NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "BinaryContent with id %s not found"
    ),

    // 400 BAD REQUEST
    USER_EMAIL_ALREADY_EXISTS(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "User with email %s already exists"
    ),
    USER_USERNAME_ALREADY_EXISTS(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "User with username %s already exists"
    ),
    READ_STATUS_ALREADY_EXISTS(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "ReadStatus with userId %s already exists"
    ),
    WRONG_PASSWORD(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "Wrong password"
    ),
    PRIVATE_CHANNEL_CANNOT_BE_UPDATED(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "Private channel cannot be updated"
    ),

    // Fallback & Standard
    NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "찾으시는 데이터가 존재하지 않습니다"
    ),
    BAD_REQUEST(
            Level.WARN,
            HttpURLConnection.HTTP_BAD_REQUEST,
            "잘못된 요청입니다"
    ),
    DATABASE_CONNECTION_FAILED(
            Level.ERROR,
            HttpURLConnection.HTTP_INTERNAL_ERROR,
            "데이터베이스 내 오류가 발생했습니다"
    );

    Level level;
    int status;
    String message;
}
