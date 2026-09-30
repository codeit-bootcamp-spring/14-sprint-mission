package com.sprint.mission.discodeit.common.multipart;

public record CreateBinaryContentCommand(String fileName,
                                         String contentType,
                                         byte[] content) {

    public static CreateBinaryContentCommand of(String fileName,
                                                String contentType,
                                                byte[] content) {
        return new CreateBinaryContentCommand(fileName, contentType, content);
    }
}
