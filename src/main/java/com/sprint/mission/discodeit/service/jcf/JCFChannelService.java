package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.dto.ChannelDto;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {
    private static final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel create(Channel channel) {
        UUID id = channel.getId();

        return findById(id).orElseGet(() -> {
            data.put(id, channel);
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
    public void update(UUID id, ChannelDto dto) {
        findById(id).ifPresent(retrieved -> retrieved.update(dto));
    }

    @Override
    public void deleteById(UUID id) {
        findById(id).ifPresent(retrieved -> data.remove(id));
    }
}
