package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends CrudRepository<Message>{
    void updateContent(UUID id, String content);

    void deleteAllByUserId(UUID id);
}
