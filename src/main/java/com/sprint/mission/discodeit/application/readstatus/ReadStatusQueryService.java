package com.sprint.mission.discodeit.application.readstatus;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.application.readstatus.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.application.readstatus.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.application.readstatus.provided.query.ReadStatusFinder;
import com.sprint.mission.discodeit.application.readstatus.required.ReadStatusRepository;
import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ExceptionType;
import com.sprint.mission.discodeit.domain.ReadStatus;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReadStatusQueryService implements ReadStatusEntityFinder, ReadStatusFinder {

  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;


  @Override
  public ReadStatus getEntityById(UUID readStatusID) {
    return readStatusRepository.findById(readStatusID)
        .orElseThrow(() -> new DiscodeitRuntimeException(
            ExceptionType.READSTATUS_NOT_FOUND));
  }

  @Override
  public List<ReadStatus> getEntitiesByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId);
  }

  @Override
  public List<ReadStatus> getEntitiesByChannelId(UUID channelId) {
    return readStatusRepository.findAllByChannel_Id(channelId);
  }

  @Override
  public List<ReadStatusDto> getByUserId(UUID userId) {
    return readStatusRepository.findAllByUser_Id(userId).stream().map(readStatusMapper::toDto)
        .toList();
  }
}
