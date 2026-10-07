package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  // User
  USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
  DUPLICATE_USER("이미 존재하는 사용자입니다."),

  // Auth
  WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),

  // UserStatus
  USER_STATUS_NOT_FOUND("사용자 상태를 찾을 수 없습니다."),
  DUPLICATE_USER_STATUS("이미 존재하는 사용자 상태입니다."),

  // Channel
  CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
  PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),

  // Message
  MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),

  // ReadStatus
  READ_STATUS_NOT_FOUND("읽음 상태를 찾을 수 없습니다."),

  // BinaryContent
  BINARY_CONTENT_NOT_FOUND("파일을 찾을 수 없습니다."),
  BINARY_CONTENT_STORAGE_ERROR("파일 저장소 처리 중 오류가 발생했습니다."),

  // Common
  INVALID_INPUT_VALUE("잘못된 입력값입니다."),
  INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다."),
  RESOURCE_NOT_FOUND("요청한 경로를 찾을 수 없습니다.");

  private final String message;
}
