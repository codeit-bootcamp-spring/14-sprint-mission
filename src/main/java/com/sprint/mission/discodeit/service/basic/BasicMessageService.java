package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    UserRepository userRepository;
    ChannelRepository channelRepository;

    public BasicMessageService(MessageRepository messageRepository,ChannelRepository channelRepository, UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void create(Message entity) {
        if(messageRepository.findById(entity.getId()) != null){
            throw new RuntimeException("이미 존재하는 매세지 입니다");
        }
        messageRepository.save(entity);
    }

    @Override
    public Message read(UUID id) {
        Message message = messageRepository.findById(id);
        if(Objects.isNull(message)){
            throw new RuntimeException("존재하지 않는 메시지입니다.");
        }

        return message;
    }


    @Override
    public void update(Message message, String newtext) {
        read(message.getId());
        message.setText(newtext);
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        read(id);
        messageRepository.deleteById(id);
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }
}
