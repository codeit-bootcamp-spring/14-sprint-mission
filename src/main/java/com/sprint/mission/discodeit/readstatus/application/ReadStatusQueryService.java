package com.sprint.mission.discodeit.readstatus.application;

import com.sprint.mission.discodeit.common.exception.DiscodeitRuntimeException;
import com.sprint.mission.discodeit.common.exception.ErrorCode;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.application.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.readstatus.application.provided.query.ReadStatusEntityFinder;
import com.sprint.mission.discodeit.readstatus.application.provided.query.ReadStatusFinder;
import com.sprint.mission.discodeit.readstatus.application.required.ReadStatusRepository;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
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
            ErrorCode.READSTATUS_NOT_FOUND));
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
