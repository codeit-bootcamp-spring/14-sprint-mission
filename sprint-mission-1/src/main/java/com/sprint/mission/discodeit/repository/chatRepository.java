package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.chatService;

import java.util.List;

public interface chatRepository extends chatService {
    Message chatCreate(User user, String message);
}
