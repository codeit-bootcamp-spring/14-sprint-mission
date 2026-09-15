package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import java.io.IOException;
import java.io.UncheckedIOException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BinaryContentMapper {
    public BinaryContentCreateRequestDto toBinaryContentCreateRequestDto(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return null;
        }
        try {
            return new BinaryContentCreateRequestDto(
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes()
            );
        } catch (IOException e) {
            throw new UncheckedIOException("파일 처리 중 오류가 발생했습니다.", e);
        }
    }

}
