package com.sprint.mission.discodeit.exception;

public class DuplicateUsernameException extends DiscodeitException{

    public DuplicateUsernameException(){
        super(ErrorCode.DUPLICATE_USERNAME);
    }

}
