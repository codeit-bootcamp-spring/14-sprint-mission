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



    FILE_NOT_FOUND(HttpStatus.UNPROCESSABLE_CONTENT, Level.INFO, "비어있거나 존재하지 않는 파일", "처리해야 하는 MultipartFile이 없거나 비어 있음", "잘못된 형식의 파일입니다."),
    FILE_IO_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, Level.ERROR, "파일 업로드 실패", "파일 업로드 실패", "파일 업로드를 실패했습니다, 잠시 후에 다시 시도하세요."),

    ;
    HttpStatus httpStatus;
    Level logLevel;
    String title;
    String description;
    String response;
}
