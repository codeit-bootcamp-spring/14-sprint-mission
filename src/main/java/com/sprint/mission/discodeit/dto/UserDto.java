package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

/**
 * 조회 결과로 내보내는 유저 정보. 엔티티와 두 가지가 다르다.
 * User에 online을 넣으면 저장할 필요도 없는 값이 도메인 모델에 들어간다. 그래서 DTO를 따로 둔다.
 */
public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        UUID profileId,
        Boolean online
) {
}
