package com.sprint.mission.discodeit.global.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum CustomErrorCode {
    INVALID_USER_INIT("계정을 생성하고 진행해주세요"),
    INVALID_USER_NAME_EMPTY("이름을 제대로 입력해주세요"),
    INVALID_MESSAGE_EMPTY("문자 메시지를 입력하여 보내주세요"),
    INVALID_MESSAGE_MAX_LENGTH("문자 메시지는 최대 500글자만 가능합니다"),

    //유저 도메인
    USER_NOT_FOUND("유저 조회를 실패하였습니다"),
    USER_DUPLICATE_EMAIL("이미 존재하는 계정입니다."),
    USER_AUTH_MISMATCH("아이디 또는 비밀번호가 일치하지 않습니다."),

    //채널 도메인
    CHANNEL_NOT_FOUND("해당 채널이 존재하지 않습니다"),
    CHANNEL_PRIVATE_CANT_UPDATE("프라이빗 채널은 수정 불가합니다"),

    //바이너리 컨텐트 서비스 도메인
    FILE_STORE_FAILED( "파일 저장에 실패하였습니다."),
    FILE_DELETE_FAILED("파일 삭제에 실패하였습니다."),
    FILE_NOT_FOUND("해당 파일을 찾을 수 없습니다."),
    FILE_EMPTY("들어온 파일이 비어있습니다"),

    //메시지 도메인
    MESSAGE_NOT_FOUND("해당 메시지를 찾을 수 없습니다."),

    //리드스테이터 도메인
    READ_STATUS_NOT_FOUND("해당 읽음상태를 찾을 수 없습니다"),

    //유저스테이터스 도메인
    USER_STATUS_NOT_FOUND_BY_USER_ID("해당 유저아이디를 필드로 가진 유저스테이터스를 찾을 수 없습니다.");

    private final String message;
}
