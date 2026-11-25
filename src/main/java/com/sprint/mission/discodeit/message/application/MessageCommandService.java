package com.sprint.mission.discodeit.message.application;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.message.application.provided.command.MessageCommand;
import com.sprint.mission.discodeit.message.application.required.MessageRepository;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.UUID;
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

  @Override
  public void deleteAllByChannelId(List<Message> messages) {
    messageRepository.deleteAll(messages);
  }

  @Override
  public void clearAuthorByUserId(UUID authorId) {
    List<Message> messages = messageRepository.findAllByAuthor_Id(authorId);
    messages.forEach(Message::clearAuthor);

  }
}
