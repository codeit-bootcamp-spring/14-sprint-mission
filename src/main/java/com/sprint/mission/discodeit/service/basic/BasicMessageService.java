package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionType;
import com.sprint.mission.discodeit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService {
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ReadStatusRepository readStatusRepository;

    public Message createMessage(String content, UUID userId, UUID channelId, List<UUID> attachmentIds) {
        if (!userRepository.existsById(userId)) {
            throw new CustomException(ExceptionType.USER_NOT_FOUND_IN_DATABASE);
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 채널입니다."));

        // PRIVATE 채널의 경우 소속된 User만 Message 생성 가능
        if (channel.getChannelType().equals(ChannelType.PRIVATE) && !readStatusRepository.existsByUserAndChannel(userId, channelId)) {
            throw new CustomException(ExceptionType.NO_ACCESS_TO_CHANNEL);
        }

        Message message = new Message(content, userId, channelId, attachmentIds);
        return messageRepository.create(message);
    }

    public Optional<Message> getMessage(UUID id) {
        return messageRepository.findById(id);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId);
    }

    // TODO 구현은 나중에, 앤티티 수정해야 해서 너무 오래 걸릴 듯,,
    public void updateMessage(UUID id, String content) {
        messageRepository.updateContent(id, content);
    }

    public void deleteMessage(UUID id) {
        // binaryContent 필드에 다른 필드의 id가 없어서, 다른 엔티티에서 조회해야하는 번거로움
        Message toBeDeleted = messageRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.MESSAGE_NOT_FOUND_IN_DATABASE));
        binaryContentRepository.delete(toBeDeleted.getAttachmentIds());

        messageRepository.deleteById(id);
    }
}
