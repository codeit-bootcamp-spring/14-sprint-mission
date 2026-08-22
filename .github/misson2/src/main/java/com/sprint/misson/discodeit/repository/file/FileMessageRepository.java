package com.sprint.misson.discodeit.repository.file;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.MessageRepository;
import java.util.List;

public class FileMessageRepository implements MessageRepository {

    @Override
    public Message create(String text, User user, Channel channel) {
        return null;
    }

    @Override
    public List<Message> readByUser(User user) {
        return List.of();
    }

    @Override
    public List<Message> readByChannel(Channel channel) {
        return List.of();
    }

    @Override
    public List<Message> readAll() {
        return List.of();
    }

    @Override
    public void update(Message message, String text) {

    }

    @Override
    public void delete(Message message) {

    }
}