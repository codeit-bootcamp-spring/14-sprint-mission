package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.binarycontent.application.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.application.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.common.DeletionEvent;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import com.sprint.mission.discodeit.message.application.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.application.provided.command.MessageCommand;
import com.sprint.mission.discodeit.message.application.provided.command.MessageModifier;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRegister;
import com.sprint.mission.discodeit.message.application.provided.command.MessageRemover;
import com.sprint.mission.discodeit.message.application.provided.command.ModifyMessageCommand;
import com.sprint.mission.discodeit.message.application.provided.command.RegisterMessageCommand;
import com.sprint.mission.discodeit.message.application.provided.query.MessageEntityFinder;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.ResolvableType;
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
  private final ApplicationEventPublisher eventPublisher;


  @Override
  public MessageDto modify(ModifyMessageCommand command) {
    Message message = messageEntityFinder.getEntityById(command.messageId());
    Message updated = messageCommand.update(message, command.newContent());
    return messageMapper.toDto(updated);
  }

  @Override
  public MessageDto register(RegisterMessageCommand command) {
    User user = userEntityFinder.getEntityById(command.authorId());
    Channel channel = channelEntityFinder.getEntityById(command.channelId());
    List<BinaryContent> binaryContents = binaryContentRegister.register(
        command.attachments() == null ? List.of() : command.attachments());

    Message message = messageCommand.create(command.content(), channel, user, binaryContents);

    log.info("메세지 생성 완료: message = {}", message);
    return messageMapper.toDto(message);
  }

  @Override
  public void remove(UUID messageId) {
    Message message = messageEntityFinder.getEntityById(messageId);
    List<UUID> attachmentsIds = message.getAttachments().stream().map(BinaryContent::getId)
        .toList();
    ResolvableType resolvableType = ResolvableType.forClassWithGenerics(List.class, UUID.class);
    DeletionEvent<List<UUID>> event = new DeletionEvent<>(List.copyOf(attachmentsIds),
        resolvableType);
    eventPublisher.publishEvent(event);
    messageCommand.delete(message);
  }

  @Override
  public void removeAllByChannelId(UUID channelId) {
    List<Message> messages = messageEntityFinder.getEntitiesByChannelId(channelId);
    for (Message m : messages) {
      remove(m.getId());
    }
  }


}
