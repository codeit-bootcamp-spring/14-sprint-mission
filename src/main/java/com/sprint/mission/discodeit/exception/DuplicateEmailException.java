package com.sprint.mission.discodeit.exception;

public class DuplicateEmailException extends DiscodeitException{

    public DuplicateEmailException() {
        super(ErrorCode.DUPLICATE_EMAIL);
    }
}
