package com.sprint.mission.discodeit.dto.channeldto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelUpdateRequestDto {
    private String channelName;
    private List<UUID> memberIds;
}
