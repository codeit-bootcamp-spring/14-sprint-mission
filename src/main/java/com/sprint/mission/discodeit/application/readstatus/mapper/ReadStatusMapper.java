package com.sprint.mission.discodeit.application.readstatus.mapper;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;
import com.sprint.mission.discodeit.domain.ReadStatus;
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
