package com.sprint.mission.discodeit.common.exception;

public class DuplicateChannelIdException extends DiscodeitException{

    public DuplicateChannelIdException(){
        super(ErrorCode.DUPLICATE_CHANNEL_ID);
    }

}
