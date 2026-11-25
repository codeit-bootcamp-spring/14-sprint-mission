package com.sprint.mission.discodeit.adapter.out.persistence.repository.message;

import com.querydsl.core.Tuple;

import java.util.List;
import java.util.UUID;

public interface MessageQueryRepository {
    List<Tuple> findLatestMessageByChannelId(List<UUID> channelIds);
}
