package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UUID;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;

public class FileMessageRepository implements MessageRepository {

    @Override
    public Message save(String text, User user, UUID channel) {
        return null;
    }

    @Override
    public List<Message> readByUser(User user) {
        return List.of();
    }

    @Override
    public List<Message> readByChannel(UUID channel) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public void update(Message message, String text) {

    }

    @Override
    public void delete(Message message) {

    }
}