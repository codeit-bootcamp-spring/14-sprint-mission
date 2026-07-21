package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.service.ChannelService;

public class JCFChannelService extends JCFService<Channel> implements ChannelService {


    @Override
    public void update(Channel channel, String newname) {
        Channel existing = read(channel.getId());
        existing.setName(newname);
    }


}
