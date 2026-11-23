package com.sprint.mission.discodeit.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ErrorCode {
  USER_NOT_FOUND(
      "존재하지 않는 유저입니다."
  ),
  CHANNEL_NOT_FOUND(
      "존재하지 않는 채널입니다."
  ),
  MESSAGE_NOT_FOUND(
      "존재하지 않는 메세지입니다."
  ),
  READSTATUS_NOT_FOUND(
      "잘못된 요청입니다."
  ),
  USER_ALREADY_EXIST(
      "이미 존재하는 유저입니다."
  ),
  CHANNEL_ALREADY_EXIST(
      "이미 존재하는 채널입니다."
  ),
  MESSAGE_ALREADY_EXIST(
      "이미 존재하는 메세지입니다."
  ),
  INVALID_INFO(
      "이름과 비밀번호를 다시 확인해주세요."
  ),
  BINARY_CONTENT_NOT_FOUND(
      "존재하지 않는 바이너리 콘텐츠입니다."
  ),
  LOGIN_FAILED(
      "이름과 비밀번호를 다시 확인해주세요."
  ),
  INTERNAL_ERROR(
      "내부 오류가 발생했습니다."
  );

  String message;
}
