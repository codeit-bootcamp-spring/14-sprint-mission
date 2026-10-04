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
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Slf4j
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
        log.debug("메세지 생성 요청: channelId={}, authorId={}",messageRequest.channelId(),messageRequest.authorId());
        Channel channel = channelRepository.findById(messageRequest.channelId())
            .orElseThrow(() -> {
                log.warn("존재하지 않는 채널에서 메세지 생성 시도: channelId={}",messageRequest.channelId());
                return new NoSuchElementException("존재하지 않는 채널입니다.");
            });
        User author = userRepository.findById(messageRequest.authorId())
            .orElseThrow(() -> {
                log.warn("존재하지 않는 유저가 메세지 생성 시도: authorId={}",messageRequest.authorId());
                return new NoSuchElementException("존재하지 않는 유저입니다.");
            });

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
        log.info("메시지 생성 완료: messageId={}, channelId={}, authorId={}",
            message.getId(), messageRequest.channelId(), messageRequest.authorId());
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
        log.debug("메세지 수정 요청: messageId={}", id);
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("존재하지 않는 메세지 수정 시도: messageId={}", id);
                return new NoSuchElementException("존재하지 않는 메시지입니다.");
            });

        message.setContent(request.newContent());
        log.info("메세지 수정 성공: messageId={}",id);
        return MessageResponseDto.from(message);
    }

    @Override
    public void delete(UUID id) {
        log.debug("메세지 삭제 요청: messageId={}", id);
        Message message = messageRepository.findById(id)
            .orElseThrow(() -> {
                log.warn("없는 메세지 삭제 시도: messageId={}",id);
                return new NoSuchElementException("없는 메시지입니다.");});


        messageRepository.deleteById(id);
        log.info("메세지 삭제 성공: messageId={}",id);
    }
}
