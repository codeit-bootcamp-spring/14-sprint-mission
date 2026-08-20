package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.MessageService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    public MessageResponseDto create(MessageCreateRequestDto messageRequest,List<BinaryContentCreateRequestDto> attachmentRequests) {
        List<UUID> attachmentIds = new ArrayList<>();
        if (attachmentRequests != null) {
            for (BinaryContentCreateRequestDto request : attachmentRequests) {
                BinaryContent binaryContent = request.toEntity();
                binaryContentRepository.save(binaryContent);
                attachmentIds.add(binaryContent.getId());
            }
        }
        Message message = messageRequest.toEntity(attachmentIds);
        messageRepository.save(message);

        return MessageResponseDto.from(message);
    }

    @Override
    public List<MessageResponseDto> findAllByChaanelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
            .map(MessageResponseDto ::from)
            .toList();
    }

    @Override
    public MessageResponseDto update(UUID id, MessageUpdateRequestDto request) {
        Message message = messageRepository.findById(id);
        if (Objects.isNull(message)) {
            throw new RuntimeException("존재하지 않는 메시지입니다.");
        }
        message.setText(request.text());
        messageRepository.save(message);
        return MessageResponseDto.from(message);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id);
        if (Objects.isNull(message)) {
            throw new RuntimeException("없는 메시지 입니다");
        }
        List<UUID> binaryContents = message.getAttachmentIds();
        if (binaryContents !=null) {
            for (UUID binaryContent : binaryContents) {
                binaryContentRepository.deleteById(binaryContent);
            }
        }
        messageRepository.deleteById(id);
    }
}
