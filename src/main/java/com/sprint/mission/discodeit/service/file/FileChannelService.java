package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final ChannelRepository channelRepository;

    // Repository를 생성자에서 주입받음
    public FileChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(Channel channel) {
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public Channel find(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(Channel channel) {
        channelRepository.save(channel);
        return channel;
    }

    @Override
    public void delete(UUID id) {
        channelRepository.delete(id);
    }
}