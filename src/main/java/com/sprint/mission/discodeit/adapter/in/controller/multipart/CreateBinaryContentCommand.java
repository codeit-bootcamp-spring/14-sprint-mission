package com.sprint.mission.discodeit.adapter.in.controller.multipart;

public record CreateBinaryContentCommand(String fileName,
                                         String contentType,
                                         byte[] content) {

    public static CreateBinaryContentCommand of(String fileName,
                                                String contentType,
                                                byte[] content) {
        return new CreateBinaryContentCommand(fileName, contentType, content);
    }
}
