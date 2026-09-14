package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.IService.MessageService;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final PageResponseMapper pageResponseMapper;

    @Override
    public MessageResponseDto create(MessageCreateRequestDto messageRequest, List<BinaryContentCreateRequestDto> attachmentRequests) {
        Channel channel = channelRepository.findById(messageRequest.channelId())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 채널입니다."));
        User author = userRepository.findById(messageRequest.authorId())
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        List<BinaryContent> attachments = new ArrayList<>();
        if (attachmentRequests != null) {
            for (BinaryContentCreateRequestDto request : attachmentRequests) {
                BinaryContent binaryContent = request.toEntity();
                binaryContentRepository.save(binaryContent);
                binaryContentStorage.put(binaryContent.getId(), request.bytes());
                attachments.add(binaryContent);
            }
        }

        Message message = messageRequest.toEntity(channel, author, attachments);
        messageRepository.save(message);

        return MessageResponseDto.from(message);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<MessageResponseDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        Slice<Message> slice = messageRepository.findAllByChannelIdOrderByCreatedAtDesc(channelId, pageable);
        return pageResponseMapper.fromSlice(slice, MessageResponseDto::from);
    }

    @Override
    public MessageResponseDto update(UUID id, MessageUpdateRequestDto request) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("존재하지 않는 메시지입니다."));

        message.setContent(request.newContent());
        return MessageResponseDto.from(message);
    }

    @Override
    public void delete(UUID id) {
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> new NoSuchElementException("없는 메시지입니다."));

        messageRepository.deleteById(id);
    }
}
