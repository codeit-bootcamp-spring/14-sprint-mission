package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.entity.ChannelType;

public class JCFChannelService extends JCFBasicService<Channel> implements ChannelService {

    ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository){
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType channelType, String memo, String memoText) {
        // 변수 네이밍 구체적으로 - 변수 네이밍
        Channel channel = new Channel(channelType, memo, memoText);
        channelRepository.save(channel);
        return channel;
    }
}
