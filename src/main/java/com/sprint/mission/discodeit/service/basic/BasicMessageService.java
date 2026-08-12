package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;

    public BasicMessageService(
            MessageRepository messageRepository,
            ChannelRepository channelRepository,
            UserRepository userRepository
    ) {
        this.messageRepository = messageRepository;
        this.channelRepository = channelRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        // 다른 서비스가 아니라 저장소를 직접 보게끔..
        // 서비스끼리 물고 물리면 어느 쪽을 먼저 만들어야 하는지가 생김.
        channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("채널을 찾을 수 없습니다! id: " + channelId));
        userRepository.findById(authorId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다! id: " + authorId));

        return messageRepository.save(new Message(content, channelId, authorId));
    }

    @Override
    public Message findById(UUID id) {
        return messageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID id, String content) {
        Message message = findById(id);
        message.update(content);
        return messageRepository.save(message);
    }

    @Override
    public void delete(UUID id) {
        findById(id);
        messageRepository.deleteById(id);
    }
}
