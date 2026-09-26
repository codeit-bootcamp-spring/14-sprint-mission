package com.sprint.mission.discodeit.application.readstatus.provided.command;

import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.application.readstatus.dto.ReadStatusDto;

public interface ReadStatusRegister {

  ReadStatusDto register(ReadStatusCreateRequest readStatusCreateRequest);
}
