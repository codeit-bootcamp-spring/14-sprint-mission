package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageRepository {
    void save(Message message);
    Message findById(UUID id); // Optional 로 감싸기
    List<Message> findAll();
    void deleteById(UUID id);
}
