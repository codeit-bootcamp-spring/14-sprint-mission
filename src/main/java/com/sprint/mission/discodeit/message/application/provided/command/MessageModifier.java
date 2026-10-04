package com.sprint.mission.discodeit.message.application.provided.command;

import com.sprint.mission.discodeit.message.application.dto.MessageDto;
import com.sprint.mission.discodeit.message.application.dto.MessageUpdateRequest;
import java.util.UUID;

public interface MessageModifier {

  MessageDto modify(UUID messageId, MessageUpdateRequest request);
}
