package com.sprint.mission.discodeit.exception;

public class AuthenticationFailedException extends DiscodeitException{

    public AuthenticationFailedException(){
        super(ErrorCode.AUTHENTICATION_FAILED);
    }
}
