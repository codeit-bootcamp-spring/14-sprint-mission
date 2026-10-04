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

  @Override
  @Transactional
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.debug("메세지 생성 시작"); /* TODO: 이럴땐 뭐찍어야하지? 질문하기 debug, trace? 아니면 컨트롤러에 info? */
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    if (!channelRepository.existsById(channelId)) {
      log.warn("조회된 채널 없음 - 채널아이디: {}", channelId);
      throw new ChannelNotFoundException(channelId);
    }
    if (!userRepository.existsById(authorId)) {
      log.warn("조회된 유저 없음 - 유저아이디: {}", authorId);
      throw new UserNotFoundException(authorId);
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
    Message target = messageRepository.save(message);
    log.debug("메세지 생성 요청 - 메세지아이디: {}", target.getId());
    return messageMapper.toDto(target, attachmentIds);
  }

  @Override
  public MessageDto find(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(message -> messageMapper.toDto(message, attachmentIds(message.getId())))
        .orElseThrow(
            () -> new MessageNotFoundException(messageId));
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page) {
    Slice<MessageDto> messages = messageRepository.findAllByChannelId(
            channelId, PageRequest.of(page, 50, Sort.by("createdAt").descending()))
        .map(message -> messageMapper.toDto(message, attachmentIds(message.getId())));
    return new PageResponse<>(
        messages.getContent(), messages.getNumber(), messages.getSize(), null);
  }

  @Override
  @Transactional
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("메세지 업데이트 시작 - 수정할 메세지아이디: {}", messageId);
    String newContent = request.newContent();
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.warn("존재하지 않는 메세지 조회- 메세지아이디: {}", messageId);
              return new MessageNotFoundException(messageId);
            });
    message.update(newContent);
    log.debug("메세지 업데이트 요청 - 메세지아이디: {}", message.getId());
    return messageMapper.toDto(message, attachmentIds(message.getId()));
  }

  @Override
  @Transactional
  public void delete(UUID messageId) {
    log.debug("메세지 삭제 시작 - 삭제할 메세지 아이디: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> {
          log.warn("존재하지 않는 메세지 조회- 메세지아이디: {}", messageId);
          return new MessageNotFoundException(messageId);
        });

    messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(MessageAttachments::getAttachment)
        .forEach(binaryContentRepository::delete);

    messageRepository.delete(message);
    log.debug("메세지 삭제 요청 - 삭제된 메세지 아이디: {}", message.getId());
  }

  private List<UUID> attachmentIds(UUID messageId) {
    return messageAttachmentsRepository.findAllByMessageId(messageId).stream()
        .map(attachment -> attachment.getAttachment().getId())
        .toList();
  }

}
