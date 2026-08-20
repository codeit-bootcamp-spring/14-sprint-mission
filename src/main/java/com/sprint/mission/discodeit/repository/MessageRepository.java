package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    void save(Message msg);
    Message findById(UUID id);
    List<Message> findAll();
    void deleteById(UUID id);
}
