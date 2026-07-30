package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicChannelService implements ChannelService {

    private final ChannelRepository channelRepository;

    public static final String ERROR_CHANNEL_NOT_FOUND = "존재하지 않는 채널입니다. ID: ";

    public BasicChannelService(ChannelRepository channelRepository) {
        this.channelRepository = channelRepository;
    }

    @Override
    public Channel create(UUID creatorId, String name) {
        Channel channel = Channel.create(creatorId, name);
        return channelRepository.save(channel);
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return channelRepository.findById(id);
    }

    @Override
    public List<Channel> readAll() {
        return channelRepository.findAll();
    }

    @Override
    public void update(UUID id, String name) {
        Channel channel = channelRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND + id));

            channel.changeName(name);
            channelRepository.save(channel);
    }

    @Override
    public void delete(UUID id) {
        channelRepository.deleteById(id);
    }
}
