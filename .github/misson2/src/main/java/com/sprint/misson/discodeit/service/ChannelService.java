package com.sprint.misson.discodeit.service;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;

public interface ChannelService {

    Channel create(ChannelType channelType, String name);

    Channel read(String channelName);

    void update(Channel channel, String updateChannel);

    void delete(String channelName);

    void readAll();
}
