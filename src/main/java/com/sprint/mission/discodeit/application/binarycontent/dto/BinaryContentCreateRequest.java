package com.sprint.mission.discodeit.application.binarycontent.dto;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public long size() {
    return bytes.length;
  }
}
