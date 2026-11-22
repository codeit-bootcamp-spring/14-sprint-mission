package com.sprint.mission.discodeit.core.application.message.port.out;

import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.message.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessagePort extends CrudRepository<Message, UUID> {
    Slice<Message> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

    Map<UUID, Instant> findLastMessageAtByChannelId(List<UUID> channelIds);

    void deleteAllByChannelId(UUID channelId);
}
