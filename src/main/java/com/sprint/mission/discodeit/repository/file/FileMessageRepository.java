package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileMessageRepository implements MessageRepository {


    @Override
    public Message save(Message message) {
        return null;
    }

    @Override
    public Optional<Message> find(UUID id) {
        return Optional.empty();
    }

    @Override
    public List<Message> findByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public List<Message> findByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public List<Message> findAll() {
        return List.of();
    }

    @Override
    public Message update(UUID id, String text) {
        return null;
    }

    @Override
    public void deleteByChannelId(UUID id) {

    }

    @Override
    public void delete(UUID uuid) {

    }
}