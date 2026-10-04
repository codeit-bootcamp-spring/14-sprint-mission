package com.sprint.mission.discodeit.common.exception;

import com.sprint.mission.discodeit.common.ErrorCode;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true)
public class DiscodeitException extends RuntimeException{
    Instant timestamp;
    ErrorCode errorCode;
    Map<String, Object> details;

    public DiscodeitException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.getMessage());
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
        this.details = details;
    }
}
