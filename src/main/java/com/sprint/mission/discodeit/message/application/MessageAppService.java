package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.binarycontent.application.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.message.application.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import com.sprint.mission.discodeit.message.application.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.application.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.application.provided.command.MessageCommand;
import com.sprint.mission.discodeit.message.application.provided.command.MessageModifier;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRegister;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRemover;
import com.sprint.mission.discodeit.message.application.provided.query.MessageEntityFinder;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class MessageAppService implements MessageRegister, MessageModifier,
    MessageRemover {

  private final MessageMapper messageMapper;
  private final MessageCommand messageCommand;
  private final ChannelEntityFinder channelEntityFinder;
  private final UserEntityFinder userEntityFinder;
  private final BinaryContentRegister binaryContentRegister;
  private final MessageEntityFinder messageEntityFinder;


  @Override
  public MessageDto modify(UUID messageId, MessageUpdateRequest request) {
    Message message = messageEntityFinder.getEntityById(messageId);
    Message updated = messageCommand.update(message, request.newContent());
    return messageMapper.toDto(updated);
  }

  @Override
  public MessageDto register(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachments) {
    User user = userEntityFinder.getEntityById(request.authorId());
    Channel channel = channelEntityFinder.getEntityById(request.channelId());
    List<BinaryContent> binaryContents = binaryContentRegister.register(
        attachments == null ? List.of() : attachments);
    Message message = messageCommand.create(request.content(), channel, user, binaryContents);
    log.info("메세지 생성 완료: message = {}", message);
    return messageMapper.toDto(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageEntityFinder.getEntityById(messageId);
    messageCommand.delete(message);
  }
}
