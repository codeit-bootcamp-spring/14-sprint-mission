package com.sprint.mission.discodeit.dto.userStatusDto;

import java.util.UUID;

public record UserStatusRequest(
    UUID uuid,
    UUID userUuid,
    boolean isOnline,
    Long createdAt,
    Long activityAt
) {

}
