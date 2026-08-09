package com.sprint.mission.discodeit.service.domain.channel;

import com.sprint.mission.discodeit.domain.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelDomainService {
    Channel create(Channel channel);
    Channel findById(UUID channelId);
    List<Channel> findAll();
    Channel update(UUID channelId, Channel channelUpdates);
    void delete(UUID channelId);
}
