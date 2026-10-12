package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChannelRepository channelRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final BinaryContentStorage binaryContentStorage;
    private final MessageMapper messageMapper;
    private final PageResponseMapper pageResponseMapper;

    @Transactional
    @Override
    public MessageDto create(MessageCreateRequest request,
        List<BinaryContentCreateRequest> attachmentRequests) {
        log.debug("메시지 생성 시작: channelId={}, authorId={}, 첨부파일 수={}",
            request.channelId(), request.authorId(), attachmentRequests.size());
        User author = userRepository.findById(request.authorId())
            .orElseThrow(() -> new UserNotFoundException(request.authorId()));
        Channel channel = channelRepository.findById(request.channelId())
            .orElseThrow(() -> new ChannelNotFoundException(request.channelId()));

        List<BinaryContent> attachments = new ArrayList<>();
        for (BinaryContentCreateRequest attachmentRequest : attachmentRequests) {
            BinaryContent attachment = new BinaryContent(
                attachmentRequest.fileName(), (long) attachmentRequest.bytes().length,
                attachmentRequest.contentType());
            binaryContentRepository.save(attachment);
            binaryContentStorage.put(attachment.getId(), attachmentRequest.bytes());
            attachments.add(attachment);
        }

        Message message = new Message(request.content(), channel, author, attachments);
        messageRepository.save(message);
        log.info("메시지 생성 완료: messageId={}, channelId={}", message.getId(), channel.getId());
        return messageMapper.toDto(message);
    }

    @Transactional(readOnly = true)
    @Override
    public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Pageable pageable) {
        Slice<MessageDto> slice = messageRepository.findAllByChannelId(channelId, pageable)
            .map(messageMapper::toDto);
        return pageResponseMapper.fromSlice(slice);
    }

    @Transactional
    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        log.debug("메시지 수정 시작: messageId={}", messageId);
        Message message = getMessage(messageId);
        message.update(request.newContent());
        log.info("메시지 수정 완료: messageId={}", messageId);
        return messageMapper.toDto(message);
    }

    @Transactional
    @Override
    public void delete(UUID messageId) {
        log.debug("메시지 삭제 시작: messageId={}", messageId);
        messageRepository.delete(getMessage(messageId));
        log.info("메시지 삭제 완료: messageId={}", messageId);
    }

    private Message getMessage(UUID messageId) {
        return messageRepository.findById(messageId)
            .orElseThrow(() -> new MessageNotFoundException(messageId));
    }
}
