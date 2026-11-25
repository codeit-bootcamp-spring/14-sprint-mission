package com.sprint.mission.discodeit.readstatus.application;

import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.readstatus.application.required.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByChannel_Id(channelId);
    readStatusRepository.deleteAll(readStatuses);
  }

  @Override
  public void deleteAllByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUser_Id(userId);
    readStatusRepository.deleteAll(readStatuses);
  }
}
