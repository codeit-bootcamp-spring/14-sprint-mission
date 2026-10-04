package com.sprint.mission.discodeit.common.multipart;

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
public enum MultiPartFileExceptionType implements IExceptionType {

    FILE_NOT_FOUND(HttpStatus.UNPROCESSABLE_CONTENT, Level.INFO, "비어있거나 존재하지 않는 파일", "처리해야 하는 MultipartFile이 없거나 비어 있음"),
    FILE_IO_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 업로드 실패", "파일 업로드 실패"),

    ;
    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
}
