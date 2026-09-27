package com.sprint.mission.discodeit.application.message.provided.query;

import com.sprint.mission.discodeit.application.message.dto.MessageCursorRequest;
import com.sprint.mission.discodeit.application.message.dto.MessageDto;
import com.sprint.mission.discodeit.common.CursorPageResponse;

public interface MessageFinder {

  CursorPageResponse<MessageDto> getByCursor(MessageCursorRequest request);
}
