package com.sprint.mission.discodeit.readstatus.application.provided.command;

import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.readstatus.application.dto.ReadStatusDto;

public interface ReadStatusRegister {

  ReadStatusDto register(ReadStatusCreateRequest readStatusCreateRequest);
}
