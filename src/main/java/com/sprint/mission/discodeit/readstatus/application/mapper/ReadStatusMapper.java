package com.sprint.mission.discodeit.readstatus.application.mapper;

import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;
import com.sprint.mission.discodeit.readstatus.domain.ReadStatus;
import org.springframework.stereotype.Component;

@Component
public class ReadStatusMapper {

  public ReadStatusDto toDto(ReadStatus readStatus) {
    return new ReadStatusDto(
        readStatus.getId(),
        readStatus.getUser().getId(),
        readStatus.getChannel().getId(),
        readStatus.getLastReadAt()
    );
  }
}
