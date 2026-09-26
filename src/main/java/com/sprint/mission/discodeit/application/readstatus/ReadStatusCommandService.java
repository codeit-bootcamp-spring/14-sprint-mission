package com.sprint.mission.discodeit.application.readstatus;

import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.application.readstatus.required.ReadStatusRepository;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadStatusCommandService implements ReadStatusCommand {

  private final ReadStatusRepository readStatusRepository;

  @Override
  public ReadStatus create(Channel channel, User user, Instant lastReadAt) {
    ReadStatus readStatus = ReadStatus.create(channel, user, lastReadAt);
    return readStatusRepository.save(readStatus);
  }

  @Override
  public List<ReadStatus> createAll(Channel channel, List<User> users, Instant lastReadAt) {
    List<ReadStatus> readStatuses = new ArrayList<>();
    for (User user : users) {
      ReadStatus readStatus = ReadStatus.create(channel, user, lastReadAt);
      readStatuses.add(readStatus);
    }
    return readStatusRepository.saveAll(readStatuses);
  }

  @Override
  public ReadStatus update(ReadStatus readStatus, Instant lastReadAt) {
    return readStatus.update(lastReadAt);
  }

  @Override
  public void delete(ReadStatus readStatus) {
    readStatusRepository.delete(readStatus);
  }
}
