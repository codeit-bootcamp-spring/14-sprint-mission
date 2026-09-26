package com.sprint.mission.discodeit.application.readstatus;

import com.sprint.mission.discodeit.application.channel.provided.query.ChannelEntityFinder;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.application.readstatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusCommand;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusModifier;
import com.sprint.mission.discodeit.application.readstatus.provided.command.ReadStatusRegister;
import com.sprint.mission.discodeit.application.readstatus.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.application.user.provided.query.UserEntityFinder;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.User;
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
