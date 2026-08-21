package com.sprint.mission.discodeit.exception;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

import java.net.HttpURLConnection;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionType {
    NOT_FOUND(
            Level.WARN,
            HttpURLConnection.HTTP_NOT_FOUND,
            "찾으시는 데이터가 존재하지 않습니다"
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
