package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ChannelService {
    Channel create(UUID creatorId, String name);
    Optional<Channel> read(UUID id);
    List<Channel> readAll();
    void update(UUID id, String name);
    void delete(UUID id);
}
