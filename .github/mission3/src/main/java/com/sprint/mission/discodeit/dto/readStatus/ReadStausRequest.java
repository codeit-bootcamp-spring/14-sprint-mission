package com.sprint.mission.discodeit.dto.readStatus;

import java.util.UUID;

public record ReadStausRequest (
    UUID uuid,
    Long recentReadAt


){

}
