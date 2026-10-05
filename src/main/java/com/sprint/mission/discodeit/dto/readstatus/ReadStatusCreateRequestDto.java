package com.sprint.mission.discodeit.dto.readstatus;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequestDto(
    @NotNull(message = "유저 ID는 필수입니다.")
    UUID userId,

    @NotNull(message = "채널 ID는 필수입니다.")
    UUID channelId,

    @NotNull(message = "마지막 읽은 시간은 필수입니다.")
    Instant lastReadAt

) {

    public ReadStatus toEntity(User user, Channel channel) {
        return new ReadStatus(user, channel, this.lastReadAt);
    }
}
