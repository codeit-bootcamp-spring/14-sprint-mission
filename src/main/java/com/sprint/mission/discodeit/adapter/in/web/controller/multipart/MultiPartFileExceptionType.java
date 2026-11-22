package com.sprint.mission.discodeit.adapter.in.web.controller.multipart;

import com.sprint.mission.discodeit.core.domain.base.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.slf4j.event.Level;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum MultiPartFileExceptionType implements ErrorCode {

    FILE_NOT_FOUND(Level.INFO, "비어있거나 존재하지 않는 파일", "처리해야 하는 MultipartFile이 없거나 비어 있음"),
    FILE_IO_FAILED(Level.ERROR, "파일 업로드 실패", "파일 업로드 실패"),

    ;
    Level logLevel;
    String title;
    String description;
}
