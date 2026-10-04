package com.sprint.mission.discodeit.binarycontent.application.dto;

public record BinaryContentCreateRequest(
    String fileName,
    String contentType,
    byte[] bytes
) {

  public long size() {
    return bytes.length;
  }
}
