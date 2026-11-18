package com.sprint.mission.discodeit.common.exception;

public class ChannelException extends DiscodeitRuntimeException {


  public ChannelException(ErrorCode errorCode) {
    super(errorCode);
  }
}
