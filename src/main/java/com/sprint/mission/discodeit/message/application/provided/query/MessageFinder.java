package com.sprint.mission.discodeit.message.application.provided.query;

import com.sprint.mission.discodeit.common.CursorPageResponse;
import com.sprint.mission.discodeit.message.application.dto.MessageCursorRequest;
import com.sprint.mission.discodeit.message.application.dto.MessageDto;

public interface MessageFinder {

  CursorPageResponse<MessageDto> getByCursor(MessageCursorRequest request);
}
