package com.sprint.mission.discodeit.message.application.provided.command;

import com.sprint.mission.discodeit.binarycontent.domain.BinaryContent;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.message.domain.Message;
import com.sprint.mission.discodeit.user.domain.User;
import java.util.List;
import java.util.UUID;

public interface MessageCommand {

  Message create(String content, Channel channel, User author, List<BinaryContent> attachments);

  Message update(Message message, String content);

  void delete(Message message);

  void deleteAllByChannelId(List<Message> messages);

  void clearAuthorByUserId(UUID authorId);
}
