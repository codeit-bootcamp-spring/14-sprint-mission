package com.sprint.mission.discodeit.common.exception;

public class DuplicateUsernameException extends DiscodeitException{

    public DuplicateUsernameException(){
        super(ErrorCode.DUPLICATE_USERNAME);
    }

}
