package com.sprint.mission.discodeit.core.application.message.out;

import com.querydsl.core.Tuple;

import java.util.List;
import java.util.UUID;

public interface MessageRepositoryCustom {
    List<Tuple> findLatestMessageByChannelId(List<UUID> channelIds);
}
