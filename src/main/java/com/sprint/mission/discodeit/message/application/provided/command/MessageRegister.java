package com.sprint.mission.discodeit.message.application.provided.command;

import com.sprint.mission.discodeit.message.application.dto.MessageDto;

public interface MessageRegister {

  MessageDto register(RegisterMessageCommand command);
}
