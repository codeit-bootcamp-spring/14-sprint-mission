package com.sprint.mission.discodeit.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public record UserDto(
    @JsonProperty("user_id")
    UUID id,
    @JsonProperty("user_username")
    String username,
    @JsonProperty("user_email")
    String email,
    @JsonProperty("user_nickName")
    String nickName,
    @JsonProperty("user_profileId")
    UUID profileId,
    boolean online,
    Instant createdAt
) {

}
