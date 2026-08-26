package com.sprint.mission.discodeit.dto.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public record MessageDto(
    UUID id,
    @JsonProperty("message_contents")
    String contents,
    UUID channelId,
    UUID authorId, // 작성자 아이디(사실상 userId와 동일)
    List<UUID> attachmentIds,
    Instant createdAt
) {

}
