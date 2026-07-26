package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.UUID;

public class JCFMessageService extends JCFBasicService<Message> implements MessageService {

    MessageRepository messageRepository;

    public JCFMessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String message, UUID ChannelId, UUID userId) {
        Message message1 = new Message(message, ChannelId, userId);
        messageRepository.save(message1);
        return message1;
    }
}
