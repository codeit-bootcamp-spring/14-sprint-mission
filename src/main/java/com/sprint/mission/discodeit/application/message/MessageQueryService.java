package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.application.message.dto.MessageCursorRequest;
import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import com.sprint.mission.discodeit.application.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.application.message.provided.query.MessageEntityFinder;
import com.sprint.mission.discodeit.application.message.provided.query.MessageFinder;
import com.sprint.mission.discodeit.application.message.provided.query.MessageTimeFinder;
import com.sprint.mission.discodeit.application.message.required.MessageQRepository;
import com.sprint.mission.discodeit.application.message.required.MessageRepository;
import com.sprint.mission.discodeit.common.CursorPageResponse;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageQueryService implements MessageEntityFinder, MessageTimeFinder, MessageFinder {

  private final MessageRepository messageRepository;
  private final MessageQRepository messageQRepository;
  private final MessageMapper messageMapper;

  @Override
  public Message getEntityById(UUID messageId) {
    return messageRepository.findById(messageId).orElseThrow(() -> new DiscodeitRuntimeException(
        ExceptionType.MESSAGE_NOT_FOUND));
  }


  @Override
  public Optional<Instant> getLastMessageAt(UUID channelId) {
    return messageRepository.findFirstByChannel_IdOrderByCreatedAtDesc(channelId)
        .map(Message::getCreatedAt);
  }

  @Override
  public CursorPageResponse<MessageDto> getByCursor(MessageCursorRequest request) {
    List<Message> messages = messageQRepository.findAllByCursor(request.channelId(),
        request.cursor(), request.idAfter(), request.size() + 1);

    boolean hasNext = messages.size() > request.size();
    Message message = null;
    if (hasNext) {
      messages.removeLast();
      message = messages.getLast();
    }

    List<MessageDto> contents = messages.stream().map(messageMapper::toDto)
        .toList();

    return CursorPageResponse.<MessageDto>builder().content(contents).size(contents.size())
        .hasNext(hasNext).nextCursor(message == null ? null : message.getCreatedAt())
        .nextIdAfter(message == null ? null : message.getId())
        .build();
  }
}
