package com.sprint.mission.discodeit.common.multipart;

import java.util.Objects;

public record CreateBinaryContentCommand(String fileName,
                                         String contentType,
                                         byte[] content) {

    public static CreateBinaryContentCommand of(String fileName,
                                                String contentType,
                                                byte[] content) {
        return new CreateBinaryContentCommand(fileName, contentType, content);
    }

    public boolean contentIsNotNull() {
        return Objects.nonNull(fileName) && Objects.nonNull(contentType)
                && Objects.nonNull(content);
    }
}
