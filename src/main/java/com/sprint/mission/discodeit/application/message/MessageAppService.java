package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.application.binarycontent.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.application.binarycontent.provided.command.BinaryContentRegister;
import com.sprint.mission.discodeit.application.channel.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.application.message.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import com.sprint.mission.discodeit.application.message.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.application.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.application.message.provided.command.MessageCommand;
import com.sprint.mission.discodeit.application.message.provided.command.MessageModifier;
import com.sprint.mission.discodeit.application.message.provided.command.MessageRegister;
import com.sprint.mission.discodeit.application.message.provided.command.MessageRemover;
import com.sprint.mission.discodeit.application.message.provided.query.MessageEntityFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
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
    return messageMapper.toDto(message);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageEntityFinder.getEntityById(messageId);
    messageCommand.delete(message);
  }
}
