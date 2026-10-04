package com.sprint.mission.discodeit.common.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ExceptionType {
    LOCAL_STORAGE_DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "디렉토리 생성 실패", "파일 로컬 저장 디렉토리 생성에 실패했습니다.", "파일 로컬 저장 디렉토리 생성에 실패했습니다."),
    LOCAL_STORAGE_FILE_ALREADY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 이미 존재", "로컬 저장소에 이미 파일이 존재합니다.", "로컬 저장소에 이미 파일이 존재합니다."),
    LOCAL_STORAGE_FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 미존재", "로컬 저장소에 파일이 존재하지 않습니다.", "로컬 저장소에 파일이 존재하지 않습니다."),
    LOCAL_STORAGE_FILE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 생성 실패", "로컬 저장소에 파일 저장을 실패했습니다.", "로컬 저장소에 파일 저장을 실패했습니다."),
    LOCAL_STORAGE_FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 조회 실패", "로컬 저장소의 파일 조회에 실패했습니다.", "로컬 저장소의 파일 조회에 실패했습니다."),



    FILE_NOT_FOUND(HttpStatus.UNPROCESSABLE_CONTENT, Level.INFO, "비어있거나 존재하지 않는 파일", "처리해야 하는 MultipartFile이 없거나 비어 있음", "잘못된 형식의 파일입니다."),
    FILE_IO_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 업로드 실패", "파일 업로드 실패", "파일 업로드를 실패했습니다, 잠시 후에 다시 시도하세요."),

    ;
    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
    String response;
}
