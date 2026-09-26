package com.sprint.mission.discodeit.common.exception;

public class DuplicateUserIdException extends DiscodeitException{

    public DuplicateUserIdException(){
        super(ErrorCode.DUPLICATE_USER_ID);
    }

}
