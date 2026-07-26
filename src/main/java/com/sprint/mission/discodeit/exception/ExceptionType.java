package com.sprint.mission.discodeit.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum ExceptionType {
    // 입력값 오류 - User
    USER_ID_IS_NULL("입력값이 비어있음", "User 수정 중 id가 비어있음", "수정할 User의 id가 입력되지 않았습니다. id를 확인해주세요."),
    USER_USERNAME_IS_NULL("입력값이 비어있음", "User 생성/수정 중 username이 빈 문자열임", "username이 입력되지 않았습니다. 빈 값이 아닌 사용자 이름을 입력해주세요."),
    USER_EMAIL_IS_NULL("입력값이 비어있음", "User 생성/수정 중 email이 빈 문자열임", "email이 입력되지 않았습니다. 빈 값이 아닌 이메일 주소를 입력해주세요."),
    USER_PASSWORD_IS_NULL("입력값이 비어있음", "User 생성/수정 중 password가 빈 문자열임", "password가 입력되지 않았습니다. 빈 값이 아닌 비밀번호를 입력해주세요."),

    // 입력값 오류 - Channel
    CHANNEL_ID_IS_NULL("입력값이 비어있음", "Channel 수정 중 id가 비어있음", "수정할 Channel의 id가 입력되지 않았습니다. id를 확인해주세요."),
    CHANNEL_NAME_IS_NULL("입력값이 비어있음", "Channel 생성/수정 중 name이 빈 문자열임", "Channel의 이름이 입력되지 않았습니다. 빈 값이 아닌 채널명을 입력해주세요."),
    CHANNEL_TYPE_IS_NULL("입력값이 비어있음", "Channel 생성/수정 중 channelType이 비어있음", "Channel의 유형이 지정되지 않았습니다. PUBLIC 또는 PRIVATE 중 하나를 선택해주세요."),

    // 입력값 오류 - Message
    MESSAGE_ID_IS_NULL("입력값이 비어있음", "Message 수정 중 id가 비어있음", "수정할 Message의 id가 입력되지 않았습니다. id를 확인해주세요."),
    MESSAGE_SENDER_ID_IS_NULL("입력값이 비어있음", "Message 생성 중 senderId가 비어있음", "메시지를 보낼 User의 id(senderId)가 입력되지 않았습니다. 발신자 정보를 확인해주세요."),
    MESSAGE_CHANNEL_ID_IS_NULL("입력값이 비어있음", "Message 생성 중 channelId가 비어있음", "메시지를 보낼 Channel의 id가 입력되지 않았습니다. 채널 정보를 확인해주세요."),
    MESSAGE_CONTENT_IS_NULL("입력값이 비어있음", "Message 생성/수정 중 content가 빈 문자열임", "메시지 내용이 입력되지 않았습니다. 빈 값이 아닌 내용을 입력해주세요."),

    // Repository 관련 - User
    USER_NOT_FOUND("User 없음", "User 조회 중 발생 - 존재하지 않는 User id", "요청하신 id에 해당하는 User를 찾을 수 없습니다. id가 정확한지, 혹은 이미 삭제된 User는 아닌지 확인해주세요."),
    USER_ALREADY_EXISTS("User 이미 존재함", "User 생성 중 발생 - 중복된 User id", "동일한 id를 가진 User가 이미 존재합니다. 다른 정보로 다시 시도해주세요."),
    USER_REPO_IS_NULL("불러올 User가 없음", "User 전체 조회 중 저장된 데이터가 없음", "현재 저장되어 있는 User가 하나도 없습니다. User를 먼저 생성한 후 다시 조회해주세요."),

    // Repository 관련 - Channel
    CHANNEL_NOT_FOUND("Channel 없음", "Channel 조회 중 발생 - 존재하지 않는 Channel id", "요청하신 id에 해당하는 Channel을 찾을 수 없습니다. id가 정확한지, 혹은 이미 삭제된 Channel은 아닌지 확인해주세요."),
    CHANNEL_ALREADY_EXISTS("Channel 이미 존재함", "Channel 생성 중 발생 - 중복된 Channel id", "동일한 id를 가진 Channel이 이미 존재합니다. 다른 정보로 다시 시도해주세요."),
    CHANNEL_REPO_IS_NULL("불러올 Channel이 없음", "Channel 전체 조회 중 저장된 데이터가 없음", "현재 저장되어 있는 Channel이 하나도 없습니다. Channel을 먼저 생성한 후 다시 조회해주세요."),

    // Repository 관련 - Message
    MESSAGE_NOT_FOUND("Message 없음", "Message 조회 중 발생 - 존재하지 않는 Message id", "요청하신 id에 해당하는 Message를 찾을 수 없습니다. id가 정확한지, 혹은 이미 삭제된 Message는 아닌지 확인해주세요."),
    MESSAGE_ALREADY_EXISTS("Message 이미 존재함", "Message 생성 중 발생 - 중복된 Message id", "동일한 id를 가진 Message가 이미 존재합니다. 다른 정보로 다시 시도해주세요."),
    MESSAGE_REPO_IS_NULL("불러올 Message가 없음", "Message 전체 조회 중 저장된 데이터가 없음", "현재 저장되어 있는 Message가 하나도 없습니다. Message를 먼저 생성한 후 다시 조회해주세요."),

    UNKNOWN_ERROR("알 수 없는 에러 발생", "예상치 못한 에러 발생", "예상치 못한 오류가 발생했습니다. 잠시 후 다시 시도해주시고, 문제가 계속되면 운영팀에 문의해주세요.");

    String detail;
    String description;
    String response;
}