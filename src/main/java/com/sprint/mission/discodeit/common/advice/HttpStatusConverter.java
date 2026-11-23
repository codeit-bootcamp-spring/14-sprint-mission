package com.sprint.mission.discodeit.common.advice;

import com.sprint.mission.discodeit.common.exception.ErrorCode;
import org.springframework.http.HttpStatus;

public class HttpStatusConverter {

  public static HttpStatus from(ErrorCode errorCode) {
    return switch (errorCode) {
      case USER_NOT_FOUND, CHANNEL_NOT_FOUND, MESSAGE_NOT_FOUND,
           READSTATUS_NOT_FOUND, BINARY_CONTENT_NOT_FOUND, INVALID_INFO -> HttpStatus.NOT_FOUND;
      case USER_ALREADY_EXIST, CHANNEL_ALREADY_EXIST, MESSAGE_ALREADY_EXIST -> HttpStatus.CONFLICT;

      case LOGIN_FAILED -> HttpStatus.UNAUTHORIZED;
      
      case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
    };
  }
}
