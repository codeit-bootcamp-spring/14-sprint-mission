package com.sprint.mission.discodeit.channel.security;

import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelAuthorization {

    private final ChannelRepository channelRepository;

    // 채널이 없으면 true로 통과시켜 서비스에서 CHANNEL_NOT_FOUND(404)를 내게 한다
    public boolean isPrivate(UUID channelId) {
        return channelRepository.findById(channelId)
            .map(channel -> channel.getType() == ChannelType.PRIVATE)
            .orElse(true);
    }
}
