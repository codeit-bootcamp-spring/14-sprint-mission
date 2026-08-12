package com.sprint.mission.discodeit.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChannelCreateRequestDto {
    private String channelName;
    private List<UUID> memberIds;

    public Channel toEntity() {
        return new Channel(this.channelName, this.memberIds, ChannelType.PUBLIC);
    }
}
