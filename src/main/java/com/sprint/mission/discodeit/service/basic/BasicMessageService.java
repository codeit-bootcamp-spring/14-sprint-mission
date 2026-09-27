package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.response.PageResponseMessageDto;
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
import lombok.RequiredArgsConstructor;
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
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  //
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageAttachmentsRepository messageAttachmentsRepository;
  private final MessageMapper messageMapper;

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " does not exist");
    }
    if (!userRepository.existsById(authorId)) {
      throw new NoSuchElementException("Author with id " + authorId + " does not exist");
    }

    Channel channel = channelRepository.getReferenceById(channelId);
    User author = userRepository.getReferenceById(authorId);

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.fileName();
          String contentType = attachmentRequest.contentType();
          byte[] bytes = attachmentRequest.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdBinaryContent = binaryContentRepository.save(binaryContent);
          return createdBinaryContent.getId();
        })
        .toList();

    String content = messageCreateRequest.content();
    Message message = new Message(
        messageCreateRequest.content(),
        channel,
        author
    );
    return messageMapper.toDto(messageRepository.save(message), attachmentIds);
  }

  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(message -> messageMapper.toDto(message, attachmentIds(message.getId())))
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
  }

  @Override
  public PageResponseMessageDto findAllByChannelId(UUID channelId, int page) {
    Slice<MessageDto> messages = messageRepository.findAllByChannelId(
            channelId, PageRequest.of(page, 50, Sort.by("createdAt").descending()))
        .map(message -> messageMapper.toDto(message, attachmentIds(message.getId())));
    return new PageResponseMessageDto(
        messages.getContent(), messages.getNumber(), messages.getSize(), null);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new NoSuchElementException("Message with id " + messageId + " not found"));
    message.update(newContent);
    return messageMapper.toDto(message, attachmentIds(message.getId()));
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new NoSuchElementException(
            "Message with id " + messageId + " not found"
        ));

    messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(MessageAttachments::getAttachment)
        .forEach(binaryContentRepository::delete);

    messageRepository.delete(message);
  }

  private List<UUID> attachmentIds(UUID messageId) {
    return messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(attachment -> attachment.getAttachment().getId())
        .toList();
  }

}
