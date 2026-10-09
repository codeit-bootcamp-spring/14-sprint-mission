package com.sprint.mission.discodeit.common.exception;

import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

public interface ExceptionType {
    HttpStatus getHttpStatus();
    Level getLogLevel();
    String getTitle();
    String getDescription();
}
