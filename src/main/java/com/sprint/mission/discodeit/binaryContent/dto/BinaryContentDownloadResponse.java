package com.sprint.mission.discodeit.binaryContent.dto;


import java.util.UUID;

public record BinaryContentDownloadResponse(
        UUID id,
        byte[] bytes,
        String filename
) {

    public static BinaryContentDownloadResponse from(UUID id, byte[] bytes, String filename){
        return new BinaryContentDownloadResponse(id, bytes, filename);
    }
}
