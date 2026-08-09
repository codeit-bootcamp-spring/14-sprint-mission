package com.sprint.mission.discodeit.exception;

public class DuplicateStatus extends DiscodeitException{

    public DuplicateStatus() {
        super(ErrorCode.DUPLICATE_STATUS);
    }
}
