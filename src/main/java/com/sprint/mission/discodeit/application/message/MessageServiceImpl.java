package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageRequest;
import com.sprint.mission.discodeit.adaptor.dto.messageDto.MessageResponse;
import com.sprint.mission.discodeit.application.channel.ChannelRepository;
import com.sprint.mission.discodeit.application.user.required.UserRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.Message;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;

  @Override
  public MessageResponse create(MessageCreateRequest messageCreateRequest) {
    if (!userRepository.existsById(messageCreateRequest.authorId())) {
      throw new DiscodeitRuntimeException(ExceptionType.USER_NOT_FOUND);
    }
    if (!channelRepository.existsById(messageCreateRequest.channelId())) {
      throw new DiscodeitRuntimeException(ExceptionType.CHANNEL_NOT_FOUND);
    }

    Message message = Message.create(messageCreateRequest);
    Message savedMessage = messageRepository.save(message);
    return MessageResponse.from(savedMessage);
  }

  @Override
  public List<MessageResponse> findByUser(UUID userId) {
    List<Message> list = messageRepository.findByUserId(userId);
    return list.stream().map(MessageResponse::from).toList();
  }

  public List<MessageResponse> findByChannel(UUID channelId) {
    List<Message> list = messageRepository.findByChannelId(channelId);
    return list.stream().map(MessageResponse::from).toList();
  }

  @Override
  public List<MessageResponse> findAll() {
    List<Message> list = messageRepository.findAll();
    return list.stream().map(MessageResponse::from).toList();
  }

  @Override
  public MessageResponse update(UUID messageId, MessageRequest messageRequest) {

    Message message = messageRepository.find(messageId).orElseThrow(
        () -> new DiscodeitRuntimeException(ExceptionType.MESSAGE_NOT_FOUND)
    );
    Message updatedMessage = message.update(messageRequest.content());
    Message savedMessage = messageRepository.save(updatedMessage);

//        Message message = messageRepository.update(messageId, messageRequest.content());
    return MessageResponse.from(savedMessage);
  }

  @Override
  public void delete(UUID uuid) {
    messageRepository.delete(uuid);
  }
}
