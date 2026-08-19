package com.sprint.mission.discodeit.web.controller.dto.res;

import com.sprint.mission.discodeit.domain.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class ChannelFindResponseDTO {
    UUID channelId;
    String channelName;
    ChannelType channelType;
    Instant latestMessageAt;
    List<UUID> userIdList;


}
