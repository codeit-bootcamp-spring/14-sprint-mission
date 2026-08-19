package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.binarycontent.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.global.exception.DiscodeitException;
import com.sprint.mission.discodeit.global.exception.ExceptionType;
import com.sprint.mission.discodeit.message.dto.MessageCreateRequestDto;
import com.sprint.mission.discodeit.message.dto.MessageResponseDto;
import com.sprint.mission.discodeit.message.dto.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.repository.UserRepository;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChannelRepository channelRepository;
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageResponseDto messageCreate(MessageCreateRequestDto messageCreateRequestDto) {
        channelRepository.findByChannel(messageCreateRequestDto.channelId())
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.CHANNEL_NOT_FOUND,
                Map.of("channelId", messageCreateRequestDto.channelId())
            ));

        userRepository.findByUser(messageCreateRequestDto.authorId())
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.USER_NOT_FOUND,
                Map.of("authorId", messageCreateRequestDto.authorId())
            ));

//        List<UUID> binaryContentsId = new ArrayList<>();
//        if (messageCreateRequestDto.attachments() != null) {
//            binaryContentsId = messageCreateRequestDto.attachments()
//                .stream()
//                .map(binaryContentRepository::toBinaryContent)
//                .map(BinaryContent::getId)
//                .toList();
//        }

        Message newMessage = new Message(messageCreateRequestDto.authorId(),
            messageCreateRequestDto.channelId(), messageCreateRequestDto.content());

        return MessageResponseDto.from(messageRepository.messageAdd(newMessage));
    }

    @Override
    public MessageResponseDto messageUpdate(UUID messageId,
        MessageUpdateRequestDto messageUpdateRequestDto) {
        Message message = messageRepository.findByMessage(messageId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)
            ));

        message.updateMessage(messageUpdateRequestDto.newContent());
        messageRepository.update(message);

        return MessageResponseDto.from(message);
    }

    @Override
    public void messageDelete(UUID messageId) {
        Message messages = messageRepository.findByMessage(messageId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)
            ));

        messages.getAttachmentIds()
            .forEach(binaryContentRepository::delete);

        messageRepository.delete(messages);
    }

    @Override
    public List<MessageResponseDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllMessage(channelId).stream()
            .map(MessageResponseDto::from)
            .toList();
    }

    @Override
    public MessageResponseDto findById(UUID messageId) {
        Message message = messageRepository.findByMessage(messageId)
            .orElseThrow(() -> new DiscodeitException(
                ExceptionType.MESSAGE_NOT_FOUND,
                Map.of("messageId", messageId)
            ));

        return MessageResponseDto.from(message);
    }
}
