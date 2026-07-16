package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.channelService;

public interface channelRepository extends channelService {
    void channelCreate(String channelName);
    Channel findByChannel(String channelName);
    void channelUpdate(String channelName, String updateChannelName);
    void channelDelete(String channelName);
}
