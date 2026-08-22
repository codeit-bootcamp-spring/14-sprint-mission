package com.sprint.misson.discodeit.service.file;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.ChannelType;
import com.sprint.misson.discodeit.repository.ChannelRepository;
import com.sprint.misson.discodeit.service.ChannelService;

public class FileChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public FileChannelService(ChannelRepository channelRepository) {
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
    public void update(Channel channel, String updateChannel) {
        channelRepository.update(channel, updateChannel);
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