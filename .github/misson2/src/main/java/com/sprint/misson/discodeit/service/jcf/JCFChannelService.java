package com.sprint.misson.discodeit.service.jcf;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.repository.ChannelRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.misson.discodeit.service.ChannelService;

public class JCFChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public JCFChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType channelType, String name) {
        return channelRepository.create(channelType, name);
    }

    @Override
    public Channel read(String channelName) {
        return channelRepository.read(channelName);
    }

    @Override
    public void update(Channel channel, String updateName) {
        channelRepository.update(channel, updateName);
    }

    @Override
    public void delete(String channelName) {
        channelRepository.delete(channelName);
    }

    @Override
    public void readAll() {
        channelRepository.readAll();
    }


}
