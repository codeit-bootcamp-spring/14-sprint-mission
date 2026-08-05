package com.sprint.mission.discodeit.dto.binaryContent;

import com.sprint.mission.discodeit.entity.BinaryContent;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

@Value
public class BinaryContentCreateDto {
    @NotNull
    byte[] content;

    public BinaryContent toBinaryContent() {
        return new BinaryContent(content);
    }
}
