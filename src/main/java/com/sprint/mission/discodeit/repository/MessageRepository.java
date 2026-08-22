package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageRepository {

    public void save(Message msg);
    public Message findById(UUID id);
    public List<Message> findAll();
    public void deleteById(UUID id);
}
