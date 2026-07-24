package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
    private static final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel create(Channel channel) {
        UUID channelId = channel.getId();

        return findById(channelId).orElseGet(() -> {
            data.put(channelId, channel);
            return channel;
        });
    }

    @Override
    public Optional<Channel> findById(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void updateName(UUID id, String name) {
        findById(id).ifPresent(retrieved -> retrieved.updateName(name));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> data.remove(id));
    }

    @Override
    public void deleteUsersByUserId(UUID userId) {
        data.values().forEach(channel -> channel.getUsersId().remove(userId));
    }

}
