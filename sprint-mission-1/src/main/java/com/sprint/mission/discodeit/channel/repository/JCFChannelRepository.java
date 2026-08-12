package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf", matchIfMissing = true)
public class JCFChannelRepository implements ChannelRepository {

    private final Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel channelAdd(Channel channel) {
        channels.put(channel.getChannelId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findByChannel(UUID channelId) {
        return Optional.ofNullable(channels.get(channelId));
    }

    @Override
    public void delete(Channel channel) {
        channels.remove(channel.getChannelId());
    }

    @Override
    public void update(Channel channel) {
        channels.replace(channel.getChannelId(), channel);
    }

    @Override
    public List<Channel> findAllChannel() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public List<Channel> findAllByType(ChannelType channelType) {
        return channels.values().stream()
            .filter(channel -> channel.getChannelType().equals(channelType))
            .toList();
    }
}
