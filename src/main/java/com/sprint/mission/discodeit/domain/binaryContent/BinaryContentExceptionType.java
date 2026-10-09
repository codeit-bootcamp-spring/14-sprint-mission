package com.sprint.mission.discodeit.domain.binaryContent;

import com.sprint.mission.discodeit.common.exception.ExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BinaryContentExceptionType implements ExceptionType {

    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, Level.INFO, "BinaryContent 미존재", "Database에서 해당 BinaryContent 찾을 수 없음", "등록되지 않은 BinaryContent입니다."),

    ;
    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
    String response;
}
