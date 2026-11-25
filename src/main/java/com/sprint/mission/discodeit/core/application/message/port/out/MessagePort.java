package com.sprint.mission.discodeit.core.application.message.port.out;

import com.sprint.mission.discodeit.core.common.pagination.PageQuery;
import com.sprint.mission.discodeit.core.common.pagination.PageResult;
import com.sprint.mission.discodeit.core.common.persistence.CrudRepository;
import com.sprint.mission.discodeit.core.domain.message.Message;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface MessagePort extends CrudRepository<Message, UUID> {
    PageResult<Message, Instant> findAllByChannelId(UUID channelId, Instant cursor, PageQuery pageQuery);

    Map<UUID, Instant> findLastMessageAtByChannelId(List<UUID> channelIds);

    void deleteAllByChannelId(UUID channelId);
}
