package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;

    private final BinaryContentRepository binaryContentRepository;


    @Override
    public MessageDto create(MessageCreateRequest request,
        List<BinaryContentCreateRequest> attachmentRequests) {


        userRepository.findById(request.authorId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.USER_NOT_FOUND,
                "해당 유저 없음." + request.authorId()));

        channelRepository.findById(request.channelId())
            .orElseThrow(() -> new DiscodeitException(ErrorCode.CHANNEL_NOT_FOUND,
                "해당 채널 없음." + request.channelId()));

        List<UUID> attachmentIds = new ArrayList<>();
        if (attachmentRequests != null) {
            for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {

                BinaryContent binaryContent = BinaryContent.builder()
                    .fileName(attachmentRequest.fileName())
                    .contentType(attachmentRequest.contentType())
                    .bytes(attachmentRequest.bytes())
                    .build();
                binaryContentRepository.save(binaryContent);
                attachmentIds.add(binaryContent.getId());
            }
        }


        Message message = Message.builder()
            .contents(request.contents())
            .authorId(request.authorId())
            .channelId(request.channelId())
            .attachmentIds(attachmentIds)
            .build();
        messageRepository.save(message);
        return toDto(message);
    }


    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        List<Message> messages
            = messageRepository.findAllByChannelId(channelId);

        List<MessageDto> result = new ArrayList<>();
        for (Message message : messages) {
            result.add(toDto(message));
        }
        return result;
    }

    @Override
    public MessageDto update(UUID id, MessageUpdateRequest request) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND,
                "메세지 없음" + id));

        message.update(request.contents());
        messageRepository.update(message);

        return toDto(message);
    }

    @Override
    public void delete(UUID id) {

        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new DiscodeitException(ErrorCode.MESSAGE_NOT_FOUND,
                "메세지 없음" + id));

        for (UUID attachmentId : message.getAttachmentIds()) {
            binaryContentRepository.delete(attachmentId);
        }
        messageRepository.delete(id);
    }


    private static MessageDto toDto(Message message) {
        return new MessageDto(
            message.getId(),
            message.getContents(),
            message.getChannelId(),
            message.getAuthorId(),
            message.getAttachmentIds(),
            message.getCreatedAt()
        );
    }
}