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
public class PrivateChannelCreateRequestDto {
    private List<UUID> memberIds;

    public Channel toEntity() {
        return new Channel(null, this.memberIds, ChannelType.PRIVATE);
    }
}
