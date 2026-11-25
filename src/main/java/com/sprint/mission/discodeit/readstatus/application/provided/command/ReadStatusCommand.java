package com.sprint.mission.discodeit.readstatus.application.provided.command;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusCommand {

  ReadStatus create(Channel channel, User user, Instant lastReadAt);

  List<ReadStatus> createAll(Channel channel, List<User> users, Instant lastReadAt);

  ReadStatus update(ReadStatus readStatus, Instant lastReadAt);

  void delete(ReadStatus readStatus);

  void deleteAllByChannelId(UUID channelId);

  void deleteAllByUserId(UUID userId);
}
