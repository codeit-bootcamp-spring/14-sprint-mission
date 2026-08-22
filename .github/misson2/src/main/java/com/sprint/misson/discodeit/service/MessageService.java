package com.sprint.misson.discodeit.service;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import java.util.List;

public interface MessageService {

    Message create(String msg, User user, Channel channel);

    List<Message> readByUser(User user);

    List<Message> readByChannel(Channel channel);

    List<Message> readAll();

    void update(Message message, String text);

    void delete(Message message);
}
