package com.sprint.mission.discodeit.adapter.out.storage;

import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum StorageExceptionType implements ErrorCode {

    LOCAL_STORAGE_DIRECTORY_CREATION_FAILED(Level.ERROR, "디렉토리 생성 실패", "파일 로컬 저장 디렉토리 생성에 실패했습니다."),
    LOCAL_STORAGE_FILE_ALREADY_EXISTS(Level.ERROR, "파일 이미 존재", "로컬 저장소에 이미 파일이 존재합니다."),
    LOCAL_STORAGE_FILE_NOT_FOUND(Level.ERROR, "파일 미존재", "로컬 저장소에 파일이 존재하지 않습니다."),
    LOCAL_STORAGE_FILE_CREATION_FAILED(Level.ERROR, "파일 생성 실패", "로컬 저장소에 파일 저장을 실패했습니다."),
    LOCAL_STORAGE_FILE_READ_FAILED(Level.ERROR, "파일 조회 실패", "로컬 저장소의 파일 조회에 실패했습니다."),

    S3_STORAGE_UPLOAD_FAILED(Level.ERROR, "파일 저장 실패", "S3 파일 업로드에 실패했습니다."),
    S3_STORAGE_READ_FAILED(Level.ERROR, "파일 조회 실패", "S3 파일 저장에 실패했습니다"),
    ;
    Level logLevel;
    String title;
    String description;
}
