package com.sprint.mission.discodeit.readstatus.application;

import com.sprint.mission.discodeit.channel.application.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.readstatus.application.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusModifier;
import com.sprint.mission.discodeit.readstatus.application.provided.command.ReadStatusRegister;
import com.sprint.mission.discodeit.readstatus.application.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import com.sprint.mission.discodeit.user.application.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.user.domain.User;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReadStatusAppService implements ReadStatusRegister, ReadStatusModifier {

  private final UserEntityFinder userEntityFinder;
  private final ChannelEntityFinder channelEntityFinder;
  private final ReadStatusEntityFinder readStatusEntityFinder;
  private final ReadStatusCommand readStatusCommand;
  private final ReadStatusMapper readStatusMapper;


  @Override
  public ReadStatusDto modify(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusEntityFinder.getEntityById(readStatusId);
    ReadStatus updated = readStatusCommand.update(readStatus,
        readStatusUpdateRequest.newLastReadAt());
    return readStatusMapper.toDto(updated);
  }

  @Override
  public ReadStatusDto register(ReadStatusCreateRequest readStatusCreateRequest) {
    User user = userEntityFinder.getEntityById(readStatusCreateRequest.userId());
    Channel channel = channelEntityFinder.getEntityById(readStatusCreateRequest.channelId());
    ReadStatus readStatus = readStatusCommand.create(channel, user, Instant.now());
    return readStatusMapper.toDto(readStatus);
  }
}
