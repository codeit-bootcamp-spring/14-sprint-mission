package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(ChannelType type, String channelName, String description) {
        return channelRepository.save(new Channel(type, channelName, description));
    }

    @Override
    public Channel findById(UUID id) {
        return channelRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다! id: " + id));
    }

    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    @Override
    public Channel update(UUID id, ChannelType type, String channelName, String description) {
        Channel channel = findById(id);
        channel.update(type, channelName, description);
        return channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        channelRepository.deleteById(id);
    }
}
