package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class JCFChannelRepository implements ChannelRepository {

    private final Map<java.util.UUID, UUID> data = new HashMap<>();

    @Override
    public Channel save(Channel channel) {
        data.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Optional<Channel> findChannel(UUID uuid) {
        return Optional.ofNullable(data.get(uuid));
    }


    @Override
    public void delete(UUID uuid) {
        data.remove(uuid);
    }

    @Override
    public List<Channel> findAll() {
        return data.values().stream().toList();
    }
}
