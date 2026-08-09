package com.sprint.mission.discodeit.exception;

public class DuplicateChannelIdException extends DiscodeitException{

    public DuplicateChannelIdException(){
        super(ErrorCode.DUPLICATE_CHANNEL_ID);
    }

}
