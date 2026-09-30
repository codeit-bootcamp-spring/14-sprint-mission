package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.channel.domain.entity.Channel;
import com.sprint.mission.discodeit.channel.web.dto.res.ChannelResponseDTO;
import com.sprint.mission.discodeit.user.domain.entity.User;
import com.sprint.mission.discodeit.user.web.dto.res.UserResponseDTO;
import java.time.Instant;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = "spring",
    uses = UserMapper.class
)
public interface ChannelMapper {

    @Mapping(target = "type", source = "channel.channelType")
    @Mapping(target = "name", source = "channel.channelName")
    ChannelResponseDTO toResponse(
        Channel channel,
        List<User> participants,
        Instant lastMessageAt
    );
}
