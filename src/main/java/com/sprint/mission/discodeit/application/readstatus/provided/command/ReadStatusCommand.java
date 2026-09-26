package com.sprint.mission.discodeit.application.readstatus.provided.command;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import java.time.Instant;
import java.util.List;

public interface ReadStatusCommand {

  ReadStatus create(Channel channel, User user, Instant lastReadAt);

  List<ReadStatus> createAll(Channel channel, List<User> users, Instant lastReadAt);

  ReadStatus update(ReadStatus readStatus, Instant lastReadAt);

  void delete(ReadStatus readStatus);
}
