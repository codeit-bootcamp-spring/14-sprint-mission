package com.sprint.mission.discodeit.core.domain.base;

import org.slf4j.event.Level;

public interface ErrorCode {
    Level getLogLevel();
    String getTitle();
    String getDescription();
}
