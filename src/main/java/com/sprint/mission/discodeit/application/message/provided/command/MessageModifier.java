package com.sprint.mission.discodeit.application.message.provided.command;

import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import com.sprint.mission.discodeit.application.message.dto.MessageUpdateRequest;
import java.util.UUID;

public interface MessageModifier {

  MessageDto modify(UUID messageId, MessageUpdateRequest request);
}
