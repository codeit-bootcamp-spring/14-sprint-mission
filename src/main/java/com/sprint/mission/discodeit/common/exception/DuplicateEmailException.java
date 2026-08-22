package com.sprint.mission.discodeit.common.exception;

public class DuplicateEmailException extends DiscodeitException{

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
