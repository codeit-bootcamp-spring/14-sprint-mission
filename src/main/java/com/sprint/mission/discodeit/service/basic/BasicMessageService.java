package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.common.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.common.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachments;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageAttachmentsRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Slice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageAttachmentsRepository messageAttachmentsRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.info("메시지 생성 요청 - 채널아이디: {} 유저아이디: {} 첨부파일 수: {}",
        messageCreateRequest.channelId(), messageCreateRequest.authorId(),
        binaryContentCreateRequests.size());
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new ChannelNotFoundException(channelId);
    }
    if (!userRepository.existsById(authorId)) {
      throw new UserNotFoundException(authorId);
    }

    Channel channel = channelRepository.getReferenceById(channelId);
    User author = userRepository.getReferenceById(authorId);

    List<BinaryContent> attachments = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          return binaryContentRepository.save(binaryContent);
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        messageCreateRequest.content(),
        channel,
        author
    );
    Message target = messageRepository.save(message);
    messageAttachmentsRepository.saveAll(attachments.stream()
        .map(attachment -> new MessageAttachments(target, attachment))
        .toList());
    return messageMapper.toDto(target, attachments);
  }

  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(message -> messageMapper.toDto(message, attachments(message.getId())))
        .orElseThrow(
            () -> new MessageNotFoundException(messageId));
  }

  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page) {
    Slice<MessageDto> messages = messageRepository.findAllByChannelId(
            channelId, PageRequest.of(page, 50, Sort.by("createdAt").descending()))
        .map(message -> messageMapper.toDto(message, attachments(message.getId())));
    return new PageResponse<>(
        messages.getContent(), messages.getNumber(), messages.getSize(), null);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.info("메시지 업데이트 요청 - 메세지아이디: {}", messageId);
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(messageId));
    message.update(newContent);
    return messageMapper.toDto(message, attachments(message.getId()));
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    log.info("메시지 삭제 요청 - 메세지아이디: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new MessageNotFoundException(messageId));

    messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(MessageAttachments::getAttachment)
        .forEach(binaryContentRepository::delete);

    messageRepository.delete(message);
  }

  private List<BinaryContent> attachments(UUID messageId) {
    return messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(MessageAttachments::getAttachment)
        .toList();
  }

}
