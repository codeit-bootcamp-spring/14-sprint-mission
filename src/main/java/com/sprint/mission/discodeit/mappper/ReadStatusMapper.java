package com.sprint.mission.discodeit.mappper;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.entity.ReadStatus;

public class ReadStatusMapper {

  ReadStatusDto toDto(ReadStatus status) {
    return new ReadStatusDto(
        status.getId(),
        status.getUser().getId(),
        status.getChannel().getId(),
        status.getLastReadAt()
    );
  }

}
