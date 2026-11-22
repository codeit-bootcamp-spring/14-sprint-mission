package com.sprint.mission.discodeit.core.domain.message;

import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MessageExceptionType implements ErrorCode {

    MESSAGE_NOT_FOUND(Level.INFO, "Message 미존재", "Database에서 해당 Message 찾을 수 없음"),
    ;

    Level logLevel;
    String title;
    String description;
}
