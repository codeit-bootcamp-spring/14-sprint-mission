package com.sprint.mission;

import org.slf4j.event.Level;

public interface ExceptionType {
    Level getLogLevel();
    int getStatus();
    String getDescription();
    String getResponse();
    String formatLogMessage(Object... errorData);
}
