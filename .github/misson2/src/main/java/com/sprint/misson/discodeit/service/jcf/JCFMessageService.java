package com.sprint.misson.discodeit.service.jcf;

import com.sprint.misson.discodeit.entity.Channel;
import com.sprint.misson.discodeit.entity.Message;
import com.sprint.misson.discodeit.entity.User;
import com.sprint.misson.discodeit.repository.MessageRepository;
import com.sprint.misson.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.misson.discodeit.service.MessageService;
import java.util.List;

public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;

    public JCFMessageService(MessageRepository messageRepository) {

        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String msg, User user, Channel channel) {
        return messageRepository.create(msg, user, channel);
    }

    @Override
    public List<Message> readByUser(User user) {
        List<Message> list = messageRepository.readByUser(user);
        list.stream()
            .forEach(msg -> System.out.println(msg));
        System.out.println(user);
        return list;
    }

    @Override
    public List<Message> readByChannel(Channel channel) {
        List<Message> list = messageRepository.readByChannel(channel);
        list.stream()
            .forEach(msg -> System.out.println(msg));
        return list;
    }

    @Override
    public List<Message> readAll() {
        List<Message> list = messageRepository.readAll();
        list.stream()
            .forEach(msg -> System.out.println(msg));
        return list;
    }

    @Override
    public void update(Message message, String text) {
        messageRepository.update(message, text);
        System.out.println("메세지 수정 완료.");
    }

    @Override
    public void delete(Message message) {
        messageRepository.delete(message);
        System.out.println("메세지 삭제 완료.");
    }
}
