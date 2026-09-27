package com.sprint.mission.discodeit.application.message;

import com.sprint.mission.discodeit.application.message.provided.command.MessageCommand;
import com.sprint.mission.discodeit.application.message.required.MessageRepository;
import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MessageCommandService implements MessageCommand {

  private final MessageRepository messageRepository;

  @Override
  public Message create(String content, Channel channel, User author,
      List<BinaryContent> attachments) {
    Message message = Message.create(content, channel, author, attachments);
    return messageRepository.save(message);
  }

  @Override
  public Message update(Message message, String content) {
    return message.update(content);
  }

  @Override
  public void delete(Message message) {
    messageRepository.delete(message);
  }
}
