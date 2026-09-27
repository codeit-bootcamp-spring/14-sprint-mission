package com.sprint.mission.discodeit.application.message.provided.command;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.domain.User;
import java.util.List;

public interface MessageCommand {

  Message create(String content, Channel channel, User author, List<BinaryContent> attachments);

  Message update(Message message, String content);

  void delete(Message message);
}
