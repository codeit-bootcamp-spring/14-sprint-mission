package com.sprint.mission.discodeit.common.multipart;

import com.sprint.mission.discodeit.common.exception.CustomException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Component
public class MultiPartFileUtil {

    public CreateBinaryContentCommand convert(MultipartFile file) {
        validateNotNullAndNotEmpty(file);
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] content = getContent(file);
        return CreateBinaryContentCommand.of(fileName, contentType, content);
    }

    private void validateNotNullAndNotEmpty(MultipartFile file) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new CustomException(ExceptionType.FILE_NOT_FOUND);
        }
    }

    private byte[] getContent(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new CustomException(ExceptionType.FILE_IO_FAILED);
        }
    }
}
