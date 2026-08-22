package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class JCFMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    @Override
    public MessageResponse create(MessageCreateRequest messageCreateRequest) {
        Message message = Message.create(messageCreateRequest);
        Message savedMessage = messageRepository.save(message);
        return MessageResponse.from(savedMessage);
    }

    @Override
    public List<MessageResponse> findByUser(UUID userId) {
        List<Message> list = messageRepository.findByUserId(userId);
        return list.stream().map(MessageResponse::from).toList();
    }

    public List<MessageResponse> findByChannel(UUID channelId) {
        List<Message> list = messageRepository.findByChannelId(channelId);
        return list.stream().map(MessageResponse::from).toList();
    }

    @Override
    public List<MessageResponse> findAll() {
        List<Message> list = messageRepository.findAll();
        return list.stream().map(MessageResponse::from).toList();
    }

    @Override
    public MessageResponse update(MessageRequest messageRequest) {
        Message message = messageRepository.update(messageRequest.uuid(), messageRequest.content());
        return MessageResponse.from(message);
    }

    @Override
    public void delete(UUID uuid) {
        messageRepository.delete(uuid);
    }
}
