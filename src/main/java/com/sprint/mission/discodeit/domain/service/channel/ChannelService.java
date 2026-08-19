package com.sprint.mission.discodeit.domain.service.channel;

import com.sprint.mission.discodeit.domain.entity.Channel;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
    Channel makeChannel(Channel channel);
    Channel findChannelById(UUID channelId);
    Channel updateChannelName(UUID channelId, String updateName);
    void deleteChannel(UUID channelId);
    List<Channel> findAllChannel();
    List<Channel> findAllChannelByIds(List<UUID> channelIdList);
    List<Channel> findAllPublicChannel();
}
