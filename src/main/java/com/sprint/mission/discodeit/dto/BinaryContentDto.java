package com.sprint.mission.discodeit.dto;

import java.util.UUID;

/** 조회 응답으로 내보내는 첨부파일 정보. bytes는 Jackson이 기본으로 base64 문자열로 직렬화한다. */
public record BinaryContentDto(
        UUID id,
        String fileName,
        String contentType,
        Long size,
        byte[] bytes
) {
}
