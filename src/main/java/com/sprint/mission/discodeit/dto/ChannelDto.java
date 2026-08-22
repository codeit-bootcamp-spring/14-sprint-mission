package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * participantIds는 Channel에 저장된 값이 아니라 ReadStatus에서 매번 파생시킨 값이다.
 * PUBLIC 채널은 항상 빈 리스트. lastMessageAt은 채널 목록을 최신순으로 보여줄 때 쓴다.
 */
public record ChannelDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String channelName,
        String description,
        List<UUID> participantIds,
        Instant lastMessageAt
) {
}
