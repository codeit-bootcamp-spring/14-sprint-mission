package com.sprint.mission.discodeit.dto;

public record AttachmentRequest(
        byte[] bytes,
        String fileName,
        String contentType
) { }