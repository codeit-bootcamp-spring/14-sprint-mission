package com.sprint.mission.discodeit.common.exception;

public class AuthenticationFailedException extends DiscodeitException{

    public AuthenticationFailedException(){
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
