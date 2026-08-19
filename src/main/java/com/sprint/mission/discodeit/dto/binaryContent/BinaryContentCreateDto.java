package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.domain.binaryContent.BinaryContent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

@Value
public class BinaryContentCreateDto {
    @NotNull
    MultipartFile content;

    public BinaryContent toBinaryContent() {
        return new BinaryContent(content);
    }
}
