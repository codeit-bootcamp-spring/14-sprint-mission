package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService extends BasicService<Message>{
    Message create(String message, UUID ChannelId, UUID userId);
}
