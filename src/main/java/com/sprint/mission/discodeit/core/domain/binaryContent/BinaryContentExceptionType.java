package com.sprint.mission.discodeit.core.domain.binaryContent;

import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum BinaryContentExceptionType implements ErrorCode {

    BINARY_CONTENT_NOT_FOUND(Level.INFO, "BinaryContent 미존재", "Database에서 해당 BinaryContent 찾을 수 없음"),

    ;
    Level logLevel;
    String title;
    String description;
}
