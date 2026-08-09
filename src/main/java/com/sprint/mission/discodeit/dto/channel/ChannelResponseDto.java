package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        ChannelType channelType,
        String title,
        String memo,
        List<UUID> userIds
) {
    public static ChannelResponseDto from(UUID id,
                                          ChannelType channelType,
                                          String title,
                                          String memo,
                                          List<UUID> userIds){
        return new ChannelResponseDto(id, channelType, title, memo, userIds);
    }
}
