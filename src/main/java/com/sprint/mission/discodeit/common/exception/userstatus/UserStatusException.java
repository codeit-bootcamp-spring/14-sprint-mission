package com.sprint.mission.discodeit.common.exception.userstatus;

import com.sprint.mission.discodeit.common.ErrorCode;
import com.sprint.mission.discodeit.common.exception.DiscodeitException;
import java.util.Map;

public class UserStatusException extends DiscodeitException {

    public UserStatusException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode, details);
    }
}
