package com.sprint.mission.discodeit.dto;

public record BinaryContentUploadRequest(
    String fileName,
    Long size,
    String contentType,
    byte[] bytes
) {

}
