package com.sprint.mission.discodeit.common.exception;

public class DuplicateStatus extends DiscodeitException{

    public DuplicateStatus() {
        super(ErrorCode.DUPLICATE_STATUS);
    }
}
