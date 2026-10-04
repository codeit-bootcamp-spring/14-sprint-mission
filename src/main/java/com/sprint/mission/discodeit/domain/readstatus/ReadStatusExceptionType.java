package com.sprint.mission.discodeit.domain.readstatus;

import com.sprint.mission.discodeit.common.exception.IExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ReadStatusExceptionType implements IExceptionType {
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, Level.INFO, "ReadStatus 미존재", "Database에서 해당 ReadStatus 찾을 수 없음"),
    READ_STATUS_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, Level.INFO, "ReadStatus 중복", "해당 ReadStatus 이미 존재"),
    ;

    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
}
