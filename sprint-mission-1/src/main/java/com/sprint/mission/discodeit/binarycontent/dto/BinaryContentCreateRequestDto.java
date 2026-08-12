package com.sprint.mission.discodeit.binarycontent.dto;

public record BinaryContentCreateRequestDto(
    String fileName,
    String contentType,
    byte[] bytes) {

}
