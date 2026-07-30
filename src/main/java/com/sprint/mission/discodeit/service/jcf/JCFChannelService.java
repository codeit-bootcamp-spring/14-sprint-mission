package com.sprint.mission.discodeit.service.jcf;

import static com.sprint.mission.discodeit.service.basic.BasicChannelService.ERROR_CHANNEL_NOT_FOUND;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JCFChannelService implements ChannelService {
    private final Map<UUID, Channel> data = new HashMap<>();

    @Override
    public Channel create(UUID creatorId, String name) {
        Channel channel = Channel.create(creatorId, name);
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> read(UUID id) {
        return Optional.ofNullable(data.get(id));
    }

    @Override
    public List<Channel> readAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(UUID id, String name) {
        Channel channel = Optional.ofNullable(data.get(id))
                .orElseThrow(() -> new IllegalArgumentException(ERROR_CHANNEL_NOT_FOUND + id));
            channel.changeName(name);
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}
