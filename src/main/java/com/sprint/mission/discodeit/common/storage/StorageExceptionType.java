package com.sprint.mission.discodeit.common.storage;

import com.sprint.mission.discodeit.common.exception.IExceptionType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum StorageExceptionType implements IExceptionType {

    LOCAL_STORAGE_DIRECTORY_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "디렉토리 생성 실패", "파일 로컬 저장 디렉토리 생성에 실패했습니다."),
    LOCAL_STORAGE_FILE_ALREADY_EXISTS(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 이미 존재", "로컬 저장소에 이미 파일이 존재합니다."),
    LOCAL_STORAGE_FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 미존재", "로컬 저장소에 파일이 존재하지 않습니다."),
    LOCAL_STORAGE_FILE_CREATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 생성 실패", "로컬 저장소에 파일 저장을 실패했습니다."),
    LOCAL_STORAGE_FILE_READ_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 조회 실패", "로컬 저장소의 파일 조회에 실패했습니다."),

    ;
    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
}
