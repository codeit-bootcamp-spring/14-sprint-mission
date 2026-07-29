package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

public interface ChannelService extends BasicService<Channel>{
    Channel create(ChannelType ChannelType, String memo, String memoText);

}
