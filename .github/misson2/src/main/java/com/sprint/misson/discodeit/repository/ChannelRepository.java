package com.sprint.misson.discodeit.repository;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.entity.User;

public interface ChannelRepository {

    Channel create(ChannelType channelType, String channelName);

    Channel read(String channelName);

    void update(Channel channel, String updateName);

    void delete(String channelName);

    void readAll();
}
