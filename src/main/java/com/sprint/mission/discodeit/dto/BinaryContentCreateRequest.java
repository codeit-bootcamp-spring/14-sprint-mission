package com.sprint.mission.discodeit.dto;

/** 프로필 이미지나 첨부파일 등록에 필요한 값들. */
public record BinaryContentCreateRequest(
        String fileName,
        String contentType,
        byte[] bytes
) {
}
