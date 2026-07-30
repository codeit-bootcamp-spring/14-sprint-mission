package com.sprint.mission.discodeit.service.basic;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.entity.Channel;
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

@Service
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(MessageRepository messageRepository,
                               ChannelRepository channelRepository,
                               UserRepository userRepository)
    {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message createMessage(String values, Channel channel, User sender) {
        //실존 유저인지, 채널있는지 검증
        if (Objects.isNull(userRepository.findById(sender.getId()))
                || Objects.isNull(channelRepository.findById(channel.getId()))) {
            throw new RuntimeException("유효하지 않은 채널 또는 유저입니다");
        }
        //샌더가 해당 채널에 존재하는지
        for (User each : channel.getMembers()) {
            if (each.getId().equals(sender.getId())) {
                Message message = new Message(values, channel, sender);
                messageRepository.save(message); // 레포지토리에 저장!
                return message;
            }
        }
        throw new RuntimeException("해당 채널의 멤버가 아닙니다" + userRepository.findById(sender.getId()));
    }

    @Override
    public Message readMessage(UUID id) {
        return messageRepository.findById(id);
    }

    @Override
    public List<Message> readAllMessage() {
        return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String values) {
        Message target = messageRepository.findById(id);
        target.setValues(values);
        target.setUpdatedAt();
        messageRepository.save(target);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.delete(id);
    }
}
