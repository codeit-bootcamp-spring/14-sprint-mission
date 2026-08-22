package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;

public interface ChannelService extends Service<Channel> {

    void update(Channel channel, String newname);


}
